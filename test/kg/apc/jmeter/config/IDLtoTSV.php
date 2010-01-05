<?php
$filename='c:/temp/remote32rpc.idl';
$file=file_get_contents($filename);

$calls=explode('RPCALL', $file);
unset($calls[0]);
unset($calls[1]);

$callno=0;
foreach($calls as $call)
{
   $map[]=trim(substr($call, 0, strpos($call, '(')))."\t".$callno++;
}

file_put_contents(dirname(__FILE__).'/'.basename($filename).'.tsv', implode("\n", $map));
?>