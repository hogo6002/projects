<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Profile extends CI_Controller {
	 public function __construct() {
        parent::__construct();
        $this->load->model('order_model');   
    }

    public function index() {
    	$this->data['name'] = $_SESSION['name'];
    	$this->data['orderArray'] = $this->order_model->get_all_orders($_SESSION['userId']);

        $this->load->view('templates/header');
        $this->load->view('profile',  $this->data);
        $this->load->view('templates/footer');
        
    }

    

}
