<?php

namespace JPGC;


class PluginsRepository extends \PWE\Modules\PWEModule implements \PWE\Modules\Outputable
{
    public function process()
    {
        if ($_POST['stats'] && $_POST['stats'] != 'null') {
            \PWE\Core\PWELogger::warn("Status: %s", $_POST['stats']);
        }
        $node = $this->PWE->getNode();
        $configsDir = $node['!a']['configs'];
        if (!$configsDir || !is_dir($configsDir)) {
            throw new \PWE\Exceptions\HTTP5xxException("Configs dir don't exist: " . realpath($configsDir));
        }

        $plugins = [];
        foreach (scandir($configsDir) as $fname) {
            if ($fname[0] == '.') {
                continue;
            }
            $set = json_decode(file_get_contents($configsDir . '/' . $fname), true);
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

        $this->PWE->sendHTTPHeader("Content-Type: application/json");
        throw new \PWE\Exceptions\HTTP2xxException(json_encode($plugins));
    }
}