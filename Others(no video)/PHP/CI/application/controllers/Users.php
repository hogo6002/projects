<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Users extends CI_Controller {
    
    public function __construct() {
        parent::__construct();
        $this->data['status'] = "";
        $this->load->model('user_model');
    }

    public function index() {
        $this->load->view('header');
        $this->load->view('home');
        $this->load->view('login_form');
        $this->load->view('footer');
    }

    public function login() {
        $username = $this->input->post('username');
        $password = $this->input->post('password');
        $remember = $this->input->post('remember');

        if ($remember) {
            setcookie("username", $_POST["username"], time() + 60*60*24, "/");            
        } else {
            delete_cookie('username');
        }

        if ($this->user_model->authenticate($username, $password)) {
            $_SESSION['username'] = $username;

            redirect(base_url(). "home/");
        } else {
            $this->data['status'] = "Your email or password is incorrect!";
            $this->index();
        }
        
    }

    public function logout() {
        session_destroy();
        redirect(base_url() . "home/");
    }
}