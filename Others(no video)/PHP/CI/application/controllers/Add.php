<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Add extends CI_Controller {
    public function __construct() {
        parent::__construct();
        $this->load->model('item_model');
        $this->load->helper('url_helper');
    }

    public function index() {
        if (isset($_SESSION["username"])) {
            $this->load->view('header');
            $this->load->view('postAd');
            $this->load->view('footer'); 
        } else {
            redirect('/users');
        }    
    }

    public function new_item() {
        $target_dir = "images/";
        $filename = basename($_FILES["fileToUpload"]["name"]);
        $target_file = $target_dir . $filename;
        $uploadOk = 1;
        $imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));

        if(isset($_POST["submit"])) {
            $check = getimagesize($_FILES["fileToUpload"]["tmp_name"]);
            if($check !== false) {
                // echo "File is an image - " . $check["mime"] . ".";
                $uploadOk = 1;
            } else {
                echo "File is not an image.";
                $uploadOk = 0;
            }
        }

        if ($uploadOk == 0) {
            echo "Sorry, your file was not uploaded.";
        } else {
            if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
                // echo "The file ". $filename. " has been uploaded.";
            } else {
                echo "Sorry, there was an error uploading your file.";
            }
        }

        $image = "./" . $target_file;
        $itemID = $this->input->post('itemID');
        $itemName = $this->input->post('itemName');
        $price = $this->input->post('price');
        $description = $this->input->post('description');
        $username = $_SESSION["username"];
        $this->item_model->add_item($itemID, $itemName, $image, $price, $username, $description);
        redirect('/item/new_post');
    }
}
