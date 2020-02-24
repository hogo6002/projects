<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Search extends CI_Controller {
    public function __construct() {
        parent::__construct();
        $this->load->model('item_model');
        $this->load->helper('url_helper');
    }

    public function index() {
        $data = array("itemName"=>null, "item"=>null);
        $itemName = $this->input->get('search');
        if(isset($itemName)){
			$data['item'] = $this->item_model->search($itemName);
		}
        $this->load->view('header');
        $this->load->view('search', $data);
        $this->load->view('footer');
    }

    public function GetCountryName(){
        $keyword=$this->input->post('keyword');
        $data=$this->item_model->GetRow($keyword);        
        echo json_encode($data);
    }

}
