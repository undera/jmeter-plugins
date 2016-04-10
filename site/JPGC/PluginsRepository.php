<?php

namespace JPGC;


class PluginsRepository extends \PWE\Modules\PWEModule implements \PWE\Modules\Outputable
{
    public function process()
    {
        if ($_SERVER['REQUEST_METHOD'] == 'POST') {
            \PWE\Core\PWELogger::warn("Stats: %s", $_POST['stats']);
        } else {
            \PWE\Core\PWELogger::warn("Repo: %s", $_GET['installID']);
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

            $this->PWE->sendHTTPHeader("Content-Type: application/json");
            throw new \PWE\Exceptions\HTTP2xxException(json_encode($plugins));
        }
    }
}