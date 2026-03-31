<?php

namespace JPGC;


use PWE\Core\PWELogger;
use PWE\Exceptions\HTTP2xxException;
use PWE\Exceptions\HTTP5xxException;
use PWE\Modules\Outputable;
use PWE\Modules\PWEModule;

class PluginsRepository extends PWEModule implements Outputable
{
    public function process()
    {
        if ($_POST['stats'] && $_POST['stats'] != 'null') {
            PWELogger::warn("Status: %s", $_POST['stats']);
            throw new HTTP2xxException("", HTTP2xxException::OK);
        } else {
            PWELogger::warn("Status: [%s]", $_REQUEST['installID']);
        }

        $repoFile = $this->getRepoFile();
        $max_age = 8 * 3600;
        $this->PWE->sendHTTPHeader("Cache-Control: public, max-age=$max_age", true);
        $this->PWE->sendHTTPHeader("Content-Type: application/json", true);
        $this->PWE->sendHTTPHeader('Last-Modified: ' . gmdate('D, d M Y H:i:s', filemtime($repoFile)) . ' GMT', true);

        if ($_REQUEST['id']) {
            $plugins = json_decode(file_get_contents($repoFile), true);
            foreach ($plugins as $plugin) {
                if ($plugin['id'] == $_REQUEST['id']) {
                    throw new HTTP2xxException(json_encode(array($plugin)));
                }
            }
        }

        throw new HTTP2xxException(file_get_contents($repoFile));
    }

    private function getRepoFile()
    {
        $node = $this->PWE->getNode();
        $configsDir = $node['!a']['configs'];
        if (!$configsDir || !is_dir($configsDir)) {
            throw new HTTP5xxException("Configs dir don't exist: " . realpath($configsDir));
        }
        $repoFile = $configsDir . '/repo.json';
        if (!is_file($repoFile)) {
            throw new HTTP5xxException("Repo file not found: " . realpath($repoFile));
        }
        return $repoFile;
    }
}
