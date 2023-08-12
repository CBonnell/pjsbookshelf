# -*- coding: utf8 -*-
import os
import subprocess
import csv
import re
import argparse

parser = argparse.ArgumentParser(description='指定された本の貸出履歴を出力.')
parser.add_argument('--unpopular', action='store_true', help='貸出回数が５回以下の図書リストを出力.')
parser.add_argument('--history', action='store_true', help='booklistで指定された本について、book historyを出力.')
parser.add_argument('--booklist', default='A1000 A1001', help='本の登録番号.')
parser.add_argument('--outdir', default='results', help='出力ディレクトリを指定.')
args = parser.parse_args()

BASE_DIR = '/home/pjs/pjs'


def _run_sql_query(query_file):
    db_dir = os.path.join(BASE_DIR, 'PJS-DB')
    jar_dir = os.path.join(BASE_DIR, 'derby')
    query_file = os.path.abspath(query_file)

    proc = subprocess.run(
            ['docker', 'run', '--rm', '-v', f'{db_dir}:/db', '-v', f'{jar_dir}:/derby:ro', '-v', f'{query_file}:/query.sql:ro', '-e', 'CLASSPATH=/derby/derby.jar:/derby/derbytools.jar', '--name', 'tosho_server', 'amazoncorretto:8', 'java', '-Dderby.system.home=/db', 'org.apache.derby.tools.ij', '/query.sql'],
            encoding='utf8', check=True, capture_output=True
    )

    return proc.stdout


results_dir = args.outdir
if not os.path.exists(results_dir):
    os.makedirs(results_dir)

def format_ij_line(line):
    if re.search('^[A-Z][0-9]{3}', line):
        line = re.sub(' +\|', '\t', line)
    elif line.find('ID') >= 0 and line.find('ij') == 0:
        line = re.sub('.+ID', 'BOOKID', line) #「BOOK」をつけたのはwindows対策：ファイルが「ID」から始まると警告が出る
        line = re.sub(' +\|', '\t', line)
        line = re.sub('\t5\t', '\tYEAR\t', line) # Only for unpopular
        line = re.sub('\t6\s+$', '\tCOUNT', line) # Only for unpopular
    else:
        print(line)
        line=''
    return line

def format_ij_and_save(ij_result, fname_out):
    with open(fname_out, 'w') as fp:
        for line in ij_result.splitlines():
            formatted_line = format_ij_line(line)
            if formatted_line != '':
                fp.write(formatted_line + '\n')

def unpopular_books():
    print('Unpopular list')
    fname_sql = './list_unpopular_books.sql'
    fname_out = os.path.join(results_dir, 'unpopular.tsv')
    ij_result = _run_sql_query(fname_sql)
    format_ij_and_save(ij_result, fname_out)

def history_books():
    booklist = args.booklist.split()
    fname_sqltemplate = './list_history_by_book.sql'
    fname_sqltemporary = './list_history_by_book.sql.tmp'
    fname_out_header = os.path.join(results_dir, 'history_')

    for bookid in booklist:
        print('Loaded:', bookid)

        # filename_sqltemplate内の:target_book_idをbookidに置き換えて、
        # filename_sqltemporaryとして保存.
        with open(fname_sqltemplate, 'r') as fp:
            sql = fp.read()
        sql = sql.replace(":target_book_id", "'" + bookid + "'")
        with open(fname_sqltemporary, 'w') as fp:
            fp.write(sql)

        ij_result = _run_sql_query(fname_sqltemporary)
        format_ij_and_save(ij_result, fname_out_header + bookid + '.tsv')

subprocess.call(['/bin/bash', '-c', os.path.join(BASE_DIR, 'stop-server.sh')])

if args.unpopular:
    unpopular_books()
if args.history:
    history_books()

subprocess.call(['/bin/bash', '-c', os.path.join(BASE_DIR, 'start-server.sh')])

