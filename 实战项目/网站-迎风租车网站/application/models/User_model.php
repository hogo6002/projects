<?php
class User_model extends CI_Model {

	public function __construct() {
		$this->load->database();
		$this->userTable = 'User';
	}
    
	public function authenticate($phone, $password) {
		$this->db->select('*');
		$this->db->from($this->userTable);
		$this->db->where('phone', $phone);
		$query  = $this->db->get();
		$row = $query->row_array();
		if (isset($row)) {
			if($this->bcrypt->check_password($password, $row['password'])){
                return FALSE;
            }else{
                return TRUE;
            }
		} else {
			return FALSE;
		}

	}

	public function valid($phone) {
		$this->db->select('*');
		$this->db->from($this->userTable);
		$this->db->where('phone', $phone);
		$query  = $this->db->get();
		// $query = $this->db->query("SELECT * FROM users WHERE username = '" . $username . "'");
		$row = $query->row_array();
		if (isset($row)) {
			return FALSE;
		} else {
			return TRUE;
		}
	}

	public function create($phone, $password, $wechatId, $name) {
		$data = array(
			'phone' => $phone,
			'password' => $this->bcrypt->hash_password($password),
			'wechatId' => $wechatId,
			'name' => $name
		);
		$this->db->insert(
			'User',
			$data
		);
	}

	public function get_user_id($phone) {
		$this->db->select('id');
		$this->db->from($this->userTable);
		$this->db->where('phone', $phone);
		$query  = $this->db->get();
		$row = $query->row_array();
		return $row['id'];
	}

	public function get_user_name($phone) {
		$this->db->select('name');
		$this->db->from($this->userTable);
		$this->db->where('phone', $phone);
		$query  = $this->db->get();
		$row = $query->row_array();
		return $row['name'];
	}

}