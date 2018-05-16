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
            throw new HTTP2xxException("", HTTP2xxException::ACCEPTED);
        } else {
            PWELogger::warn("Status: [%s]", $_REQUEST['installID']);
        }
        $node = $this->PWE->getNode();
        $configsDir = $node['!a']['configs'];
        if (!$configsDir || !is_dir($configsDir)) {
            throw new HTTP5xxException("Configs dir don't exist: " . realpath($configsDir));
        }

        $plugins = [];
        $oldest = 0;
        foreach (scandir($configsDir) as $fname) {
            if ($fname[0] == '.') {
                continue;
            }
            $full_name = $configsDir . '/' . $fname;
            $oldest = max($oldest, filemtime($full_name));
            $set = json_decode(file_get_contents($full_name), true);
            $plugins = array_merge($plugins, $set);
        }

        if ($_REQUEST['id']) {
            foreach ($plugins as $plugin) {
                if ($plugin['id'] == $_REQUEST['id']) {
                    $plugins = array($plugin);
                    break;
                }
            }
        }

        $this->PWE->sendHTTPHeader('Last-Modified: ' . gmdate('D, d M Y H:i:s', $oldest) . ' GMT');
        $max_age = 24 * 3600;
        $this->PWE->sendHTTPHeader("Cache-Control: public, max-age=$max_age");

        $this->PWE->sendHTTPHeader("Content-Type: application/json");
        throw new HTTP2xxException(json_encode($plugins));
    }
}
