<?php

if ($_SERVER['SERVER_ADDR'] == $_SERVER['REMOTE_ADDR']) {
  $level = \PWE\Core\PWELogger::DEBUG;
  $logfile = "/tmp/jpgc-pwe.log";
  $tempdir = "/tmp";
} else {
  $level = \PWE\Core\PWELogger::WARNING;
  $logfile = "/home/ofhrhtgk/tmp/pwe.log";
  $tempdir = "/home/ofhrhtgk/tmp";
}

\PWE\Core\PWELogger::setStdErr($logfile);
\PWE\Core\PWELogger::setStdOut($logfile);
\PWE\Core\PWELogger::setLevel($level);

/** @var $PWECore PWE\Core\PWECore */
$PWECore->setRootDirectory(__DIR__);
$PWECore->setXMLDirectory($PWECore->getDataDirectory());
$PWECore->setTempDirectory($tempdir);
