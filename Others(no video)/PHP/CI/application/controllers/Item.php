<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Item extends CI_Controller {
    public function __construct() {
        parent::__construct();
        $this->load->model('item_model');
        $this->load->helper('url_helper');
        $this->data['exist'] = FALSE;
    }

    public function index() {
        $this->load->view('header');
        $this->load->view('item',  $this->data);
        $this->load->view('footer');   
    }

    public function load() {
        $itemID = $this->input->post('itemID');
        if ($this->item_model->exist($itemID)) {
            if (isset($_SESSION['username'])){
                $username = $_SESSION['username'];
                if ($this->item_model->check_wishlist($username, $itemID)) {
                    $this->data['exist'] = TRUE;
                }
            }
                
            $this->data['itemID'] = $itemID;
            $this->data['itemName'] = $this->item_model->get_info($itemID)['itemName'];
            $this->data['image'] = $this->item_model->get_info($itemID)['image'];
            $this->data['price'] = $this->item_model->get_info($itemID)['price'];
            $this->data['description'] = $this->item_model->get_info($itemID)['description'];
            $this->data['saller'] = $this->item_model->new_item()['saller'];
            $this->index();
        } else {

        }
    }

    public function new_post() {
        $this->data['itemName'] = $this->item_model->new_item()['itemName'];
        $this->data['itemID'] = $this->item_model->new_item()['itemID'];
        $this->data['image'] = $this->item_model->new_item()['image'];
        $this->data['saller'] = $this->item_model->new_item()['saller'];
        $this->data['price'] = $this->item_model->new_item()['price'];
        $this->data['description'] = $this->item_model->new_item()['description'];
        $this->index();
    }

}
