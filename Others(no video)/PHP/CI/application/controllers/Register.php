<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Register extends CI_Controller {
    
    public function __construct() {
        parent::__construct();
        $this->data['status'] = "";
        $this->load->model('user_model');
    }

    public function index() {
        $this->load->view('header');
        $this->load->view('home');
        $this->load->view('register_form');
        $this->load->view('footer');
    }

    public function register() {
        $username = $this->input->post('username');
        $password = $this->input->post('password');
        $firstname = $this->input->post('firstname');
        $lastname = $this->input->post('lastname');
        if ($this->user_model->valid($username)) {
            $this->user_model->create($username, $password, $firstname, $lastname);
            redirect(base_url() . "users/");
        } else {
            redirect(base_url() . "register/");
        }
    }

    
}