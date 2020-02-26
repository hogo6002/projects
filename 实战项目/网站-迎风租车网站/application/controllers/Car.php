<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Car extends CI_Controller {
	 public function __construct() {
        parent::__construct();
        $this->load->model('car_model');   
        $this->load->model('order_model');  
    }

    public function index() {
    	$this->data['carArray'] = $this->car_model->get_all_cars();
        $this->load->view('templates/header');
        $this->load->view('all_cars',  $this->data);
        $this->load->view('templates/footer');
        
    }

    public function search() {
    	$startDate = $this->input->get('pick-up-date');
    	$startTime = $this->input->get('pick-up-time');
    	$startTime = date('Y-m-d H:i:s', strtotime("$startDate $startTime")) . '.000';

    	$endDate = $this->input->get('drop-off-date');
    	$endTime = $this->input->get('drop-off-time');
    	$endTime = date('Y-m-d H:i:s', strtotime("$endDate $endTime")) . '.000';

    	if(isset($startTime) && isset($endTime)){
    		$this->data['carArray'] = $this->car_model->search_cars($startTime, $endTime);
    	}
    	$this->data['startTime'] = $startTime;
    	$this->data['endTime'] = $endTime;

        $this->load->view('templates/header');
        $this->load->view('search_cars',  $this->data);
        $this->load->view('templates/footer');
        
    }

    public function detail() {
        $startTime = $this->input->get('startTime');
        $endTime = $this->input->get('endTime');
        $id = $this->input->get('carId');

        $status = $this->input->get('status');

        if(isset($id)){
            $this->data['carArray'] = $this->car_model->get_car($id);
        }
        $this->data['startTime'] = $startTime;
        $this->data['endTime'] = $endTime;
        $this->data['status'] = $status;
        $this->data['id'] = $id;

        $this->load->view('templates/header');
        $this->load->view('car_detail',  $this->data);
        $this->load->view('templates/footer');   
    }

    public function submit() {
        $startTime = $this->input->get('startTime');
        $endTime = $this->input->get('endTime');
        $carId = $this->input->get('carId');
        $userId = $_SESSION['userId'];
        $price = $this->input->get('price');
        $this->order_model->create_order($startTime, $endTime, $carId, $userId, $price);
         redirect(base_url(). "index.php/profile/");
    }

}
