#!/bin/sh
echo "PHP CheckStyle script"
# php run.php --src ./test/sample/issuegithub32.php --outdir ./checkstyle_result --config default.cfg.xml --format html,xml --linecount --debug
php run.php --src ./src --outdir ./checkstyle_result --config default.cfg.xml --format html,xml --linecount

