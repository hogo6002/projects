<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Wishlist extends CI_Controller {
    public function __construct() {
        parent::__construct();
        $this->load->model('item_model');
        $this->load->model('user_model');
        $this->load->model('account_model');
        $this->load->helper('url_helper');
    }


    public function index() { 
        if (isset($_SESSION["username"])) {
            $data = array("itemName"=>null, "item"=>null, "user"=>null);
            $username = $_SESSION['username'];
            $data['user'] = $this->account_model->get_info($username);
            $data['item'] = $this->item_model->get_items($username);
            $this->load->view('header');
            $this->load->view('wishlist', $data);
            $this->load->view('footer');   
        } else {
            redirect('/users');
        }
    }



    public function load() {
        $this->item_model->get_info();
    }

    public function add_item() {
        $this->data['username'] = $_SESSION['username'];
        $this->data['itemID'] = $this->input->post('itemID');
        $this->item_model->add_wishlist($this->data['username'], $this->data['itemID']);
        $this->index();

    }

    public function remove_item() {
        $this->data['username'] = $_SESSION['username'];
        $this->data['itemID'] = $this->input->post('itemID');
        $this->item_model->remove_wishlist($this->data['username'], $this->data['itemID']);
        $this->index();
    }
}
