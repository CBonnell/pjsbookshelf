#! /bin/sh

set -o errexit
set -o nounset

BACKUP_DEST=/home/pjs/Dropbox/PJS-DB-backup/
PJS_BASEDIR=/home/pjs/pjs
PJSDB=$PJS_BASEDIR/PJS-DB

$PJS_BASEDIR/stop-server.sh

# Set the PATH.
export PATH="/usr/sbin:/sbin:/usr/bin:/bin"

echo KOKO1 > /tmp/koko
# Check the internet connection.
if ! ping -c 1 www.dropbox.com 2>&1 | grep transmitted > /dev/null; then
    echo 'No network connection'
    $PJS_BASEDIR/start-server.sh
    exit 1
fi
echo KOKO2 >> /tmp/koko

# Start Dropbox
dropbox start > /dev/null

# Copy the database files.
echo "copying ${PJSDB} to ${BACKUP_DEST}..."
if test -d ${BACKUP_DEST}/PJS-DB.bak; then
    rm -rf ${BACKUP_DEST}/PJS-DB.bak.bak
    mv ${BACKUP_DEST}/PJS-DB.bak ${BACKUP_DEST}/PJS-DB.bak.bak
fi
if test -d ${BACKUP_DEST}/PJS-DB; then
    rm -rf ${BACKUP_DEST}/PJS-DB.bak
    mv ${BACKUP_DEST}/PJS-DB ${BACKUP_DEST}/PJS-DB.bak
fi
rsync -rlptvz ${PJSDB} ${BACKUP_DEST}

# Wait until dropbox sync is done.
while true; do
    status=$(dropbox status | head -1)
    echo $status
    case $status in
        Idle*|最新の状態*) break ;;
        *) sleep 2;;
    esac        
done

$PJS_BASEDIR/start-server.sh

exit 0
