<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Register extends CI_Controller {
    
    public function __construct() {
        parent::__construct();
        $this->data['status'] = "";
        $this->load->model('user_model');
    }

    public function index() {
        $this->load->view('templates/header');
        $this->load->view('home');
        $this->load->view('register_form');
        $this->load->view('templates/footer');
    }

    public function submit() {
        $phone = $this->input->post('phone');
        $password = $this->input->post('password');
        $wechatId = $this->input->post('wechatId');
        $name = $this->input->post('name');
        if ($this->user_model->valid($phone)) {
            $this->user_model->create($phone, $password, $wechatId, $name);
            redirect(base_url() . "index.php/login/");
        } else {
            redirect(base_url() . "index.php/register/");
        }
    }

    
}