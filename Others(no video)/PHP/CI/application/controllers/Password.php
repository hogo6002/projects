<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Password extends CI_Controller {
    public function __construct() {
        parent::__construct();
        $this->load->model('account_model');
        $this->load->helper('url_helper');
    }


    public function index() {
        $this->load->view('header');
        $this->load->view('changePass');
        $this->load->view('footer');
    }

    public function update() {
        $username = $this->input->post('username');
        $newpw = $this->input->post('newpw');
        $conpw = $this->input->post('conpw');
        $name = $this->input->post('name');
        if ($this->account_model->valid($username, $name) && ($newpw === $conpw)) {
            $this->account_model->changePass($username, $newpw); 
            redirect('/users');
        } else {
            $this->index();
        }
        
    }

   
    

    
}