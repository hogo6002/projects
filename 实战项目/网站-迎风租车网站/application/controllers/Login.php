<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Login extends CI_Controller {
    
    public function __construct() {
        parent::__construct();
        $this->data['status'] = "";
        $this->load->model('user_model');
    }

    public function index() {
        $this->load->view('templates/header');
        $this->load->view('home');
        $this->load->view('login_form');
        $this->load->view('templates/footer');
    }

    public function submit() {
        $phone = $this->input->post('phone');
        $password = $this->input->post('password');
        $remember = $this->input->post('remember');

        if ($remember) {
            setcookie("phone", $_POST["phone"], time() + 60*60*24, "/");            
        } else {
            delete_cookie('phone');
        }

        if ($this->user_model->authenticate($phone, $password)) {
            $_SESSION['phone'] = $phone;
            $_SESSION['userId'] = $this->user_model->get_user_id($phone);
            $_SESSION['name'] = $this->user_model->get_user_name($phone);

            redirect(base_url(). "index.php/home/");
        } else {
            $this->data['status'] = "Your email or password is incorrect!";
            $this->index();
        }
        
    }

    public function logout() {
        session_destroy();
        redirect(base_url() . "index.php/home/");
    }
}