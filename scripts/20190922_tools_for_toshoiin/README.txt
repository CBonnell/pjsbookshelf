# 過去５年間における貸出回数の少ない本のリストを取得する
# 出力ファイル：results/unpopular.tsv
python list_unpopular_and_history.py --unpopular

# 特定の本の貸出履歴を取得する
# （--booklistの後ろの本IDを検索したい本のIDに書き換えてください）
# 出力ファイル：results/history_[bookid].tsv (例：results/history_A1000.tsv)
python list_unpopular_and_history.py --history --booklist 'A1000 C132-01 D119'
