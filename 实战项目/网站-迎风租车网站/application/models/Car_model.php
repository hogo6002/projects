<?php
class Car_model extends CI_Model {

	public function __construct() {
		$this->load->database();
		$this->userTable = 'User';
		$this->carTable = 'Car';
	}
    
	public function get_all_cars() {
		$this->db->select('*');
		$this->db->from($this->carTable);
		$query  = $this->db->get();
		$carArray = array();
		$result = $query->result();
		for ($index = 1; $index <= sizeof($result); $index++) {
			$row = $result[$index - 1];
			$carArray[$index] = $row->carName . "||" . $row->price . "||" . $row->image . "||" . $row->id;
		}
		return $carArray;
	}

	public function search_cars($startTime, $endTime) {
		$query  = $this->db->query("SELECT * FROM Car where id Not in 
                (SELECT carId FROM Booking where (startTime <= '" . $startTime . "' and endTime >= '" . $startTime . "')
                OR (startTime <= '" . $endTime . "' and endTime >= '" . $endTime . "')
                )
            ");
		$carArray = array();
		$result = $query->result();
		for ($index = 1; $index <= sizeof($result); $index++) {
			$row = $result[$index - 1];
			$carArray[$index] = $row->carName . "||" . $row->price . "||" . $row->image . "||" . $row->id;
		}
		return $carArray;
	}

	public function get_car($id) {
		$query  = $this->db->query("SELECT * FROM Car where id = ". $id . "
            ");
		$carArray = array();
		$result = $query->result();
		for ($index = 1; $index <= sizeof($result); $index++) {
			$row = $result[$index - 1];
			$carArray['carName'] = $row->carName;
			$carArray['price'] = $row->price;
			$carArray['image'] = $row->image;
		}
		return $carArray;
	}

}