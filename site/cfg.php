<?php

if ($_SERVER['SERVER_ADDR'] == $_SERVER['REMOTE_ADDR']) {
    $level = \PWE\Core\PWELogger::DEBUG;
    $logfile = sys_get_temp_dir() . "/jpgc-pwe.log";
    $tempdir = sys_get_temp_dir();
} else {
    $level = \PWE\Core\PWELogger::WARNING;
    $logfile = __DIR__."/../logs/pwe." . date('Ym');
    $tempdir = __DIR__."/../tmp";
}

\PWE\Core\PWELogger::setStdErr($logfile);
\PWE\Core\PWELogger::setStdOut($logfile);
\PWE\Core\PWELogger::setLevel($level);

/** @var $PWECore PWE\Core\PWECore */
$PWECore->setRootDirectory(__DIR__);
$PWECore->setXMLDirectory($PWECore->getDataDirectory());
$PWECore->setTempDirectory($tempdir);
