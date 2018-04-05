<?php
require_once __DIR__ . '/vendor/autoload.php';

$isDebug = $_SERVER['SERVER_PORT'] != 80;
if (!$isDebug) {
    // our real website settings
    $level = \PWE\Core\PWELogger::WARNING;
    $logfile = __DIR__ . "/../logs/pwe." . date('Ym');
    $tempdir = __DIR__ . "/../tmp";
} else {
    // local debugging settings
    $level = \PWE\Core\PWELogger::DEBUG;
    $tempdir = sys_get_temp_dir();
    $logfile = "/tmp/jpgc-pwe.log";
}

\PWE\Core\PWELogger::setStdErr($logfile);
\PWE\Core\PWELogger::setStdOut($logfile);
\PWE\Core\PWELogger::setLevel($level);

/** @var $PWECore PWE\Core\PWECore */
$PWECore->setRootDirectory(__DIR__);
$PWECore->setXMLDirectory($PWECore->getDataDirectory());
$PWECore->setTempDirectory($tempdir);

if ($isDebug) {
    $fname = $tempdir . '/jpgc.xml';
    if (!is_file($fname)) {
        file_put_contents($fname, "<registry/>");
    }

    $PWECore->getModulesManager()->setRegistryFile($fname);
}
