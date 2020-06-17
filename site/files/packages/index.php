<?php
header('Content-Type: application/json');

$dir          = __DIR__; //path

$list = array(); //main array

if(is_dir($dir)){
    if($dh = opendir($dir)){
        while(($file = readdir($dh)) != false){

            if($file == "." or $file == ".."){
                //...
            } else { //create object with two fields
                $list3 = array(
                'file' => $file, 
                'size' => filesize($file));
                array_push($list, $list3);
            }
        }
    }

    $return_array = array('files'=> $list);

    echo json_encode($return_array);
}
