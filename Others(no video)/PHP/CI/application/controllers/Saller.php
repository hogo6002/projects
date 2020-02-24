<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Saller extends CI_Controller {

    public function __construct() {
        parent::__construct();
        $this->load->model('account_model');
        $this->load->helper('url_helper');
        // if (isset($_SESSION["username"])) {
        //     $this->data['username'] = $_SESSION["username"];
        // $this->data['password'] = $this->account_model->get_info($this->data['username'])['password'];
        // $this->data['firstname'] = $this->account_model->get_info($this->data['username'])['firstname'];
        // $this->data['lastname'] = $this->account_model->get_info($this->data['username'])['lastname'];
        // $this->data['profile'] = $this->account_model->get_info($this->data['username'])['profile'];
        // } else {
        //     redirect('/users');
        // }
    }

    public function index() {
        $saller = $this->input->post('username');
        $data = array("itemName"=>null, "review"=>null, "user"=>null);
        $data['user'] = $this->account_model->get_info($saller);
        $data['review'] = $this->account_model->reviews($saller);
        
        $this->load->view('header');
        $this->load->view('saller', $data);
        $this->load->view('footer');
    }

    public function add_comment() {
        $saller = $this->input->post('username');
        $rating = $this->input->post('rating');
        $title = $this->input->post('title');
        $comment = $this->input->post('comment');
        $username = $_SESSION["username"];
        $this->account_model->add_comment($username, $rating, $title, $comment, $saller);
        $this->index();
    }

    


    // public function update() {
    //     $firstname = $this->input->post('firstname');
    //     $username = $this->data['username'];
    //     $this->data['firstname'] = $firstname;
    //     $this->account_model->update($username, $firstname);
    //     $this->index();
    // }

    // public function do_upload() {
    //     $target_dir = "images/";


    //     $filename = basename($_FILES["fileToUpload"]["name"]);


    //     $username = $this->data['username'];

    //     $target_file = $target_dir . $filename;
    //     $uploadOk = 1;
    //     $imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));

    //     if(isset($_POST["submit"])) {
    //         $check = getimagesize($_FILES["fileToUpload"]["tmp_name"]);
    //         if($check !== false) {
    //             echo "File is an image - " . $check["mime"] . ".";
    //             $uploadOk = 1;
    //         } else {
    //             echo "File is not an image.";
    //             $uploadOk = 0;
    //         }
    //     }

    //     if ($uploadOk == 0) {
    //         echo "Sorry, your file was not uploaded.";
    //     } else {
    //         if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
    //             echo "The file ". $filename. " has been uploaded.";
    //         } else {
    //             echo "Sorry, there was an error uploading your file.";
    //         }
    //     }

    //     $this->account_model->upload($username, "./".$target_file);
    //     redirect(base_url() . "profile/");
    // }

   
    

    
}