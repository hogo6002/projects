<?php
class Order_model extends CI_Model {

	public function __construct() {
		$this->load->database();
		$this->userTable = 'User';
		$this->bookingTable = 'Booking';
		$this->carTable = 'Car';
	}
    
	public function create_order($startTime, $endTime, $carId, $userId
		, $price) {
		$data = array(
			'startTime' => $startTime,
			'endTime' => $endTime,
			'carId' => $carId,
			'userId' => $userId,
			'price' => $price,
			'finished' => '0'
		);
		$this->db->insert(
			'Booking',
			$data
		);
	}

	public function get_all_orders($userId) {
		$this->db->select('*');
		$this->db->from($this->bookingTable);
		$this->db->where('userId', $userId);
		$query  = $this->db->get();
		$orderArray = array();
		$result = $query->result();
		for ($index = 1; $index <= sizeof($result); $index++) {
			$row = $result[$index - 1];
			$this->db->select('*');
			$this->db->from($this->carTable);
			$this->db->where('id', $row->carId);
			$query  = $this->db->get();
			$result1 = $query->result();
			$row1 = $result1[0];
			$orderArray[$index] = $row1->carName . "||" . $row->startTime . "||" . $row->endTime . "||" . $row->price;
		}
		return $orderArray;
	}
}