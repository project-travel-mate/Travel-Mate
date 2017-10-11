echo "PHP Checkstyle script"
php -c C:\ms4w\Apache\cgi-bin\php.ini run.php --src ./test/issue56.php --outdir ./checkstyle_result --format html,xml --linecount --debug
pause
