<?php

if ($_SERVER['SERVER_ADDR'] == $_SERVER['REMOTE_ADDR']) {
  $level = \PWE\Core\PWELogger::DEBUG;
} else {
  $level = \PWE\Core\PWELogger::WARNING;
}
$logfile = sys_get_temp_dir() . "/pwe.".date('Ym');
$tempdir = sys_get_temp_dir();

\PWE\Core\PWELogger::setStdErr($logfile);
\PWE\Core\PWELogger::setStdOut($logfile);
\PWE\Core\PWELogger::setLevel($level);

/** @var $PWECore PWE\Core\PWECore */
$PWECore->setRootDirectory(__DIR__);
$PWECore->setXMLDirectory($PWECore->getDataDirectory());
$PWECore->setTempDirectory($tempdir);
