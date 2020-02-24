<?php
class User_model extends CI_Model {

	public function __construct() {
		$this->load->database();
		$this->userTable = 'users';
	}
    
	public function authenticate($username, $password) {
		// $query = $this->db->query("SELECT * FROM users WHERE username = '" . $username . "'");
		$this->db->select('*');
		$this->db->from($this->userTable);
		$this->db->where('username', $username);
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

	public function valid($username) {
		$this->db->select('*');
		$this->db->from($this->userTable);
		$this->db->where('username', $username);
		$query  = $this->db->get();
		// $query = $this->db->query("SELECT * FROM users WHERE username = '" . $username . "'");
		$row = $query->row_array();
		if (isset($row)) {
			return FALSE;
		} else {
			return TRUE;
		}
	}

	public function create($username, $password, $firstname, $lastname) {
		$profile = "./images/profile.png";
		$data = array(
			'username' => $username,
			'password' => $this->bcrypt->hash_password($password),
			'firstname' => $firstname,
			'lastname' => $lastname,
			'profile' => $profile
		);
		$this->db->insert(
			'users',
			$data
		);
	}

}