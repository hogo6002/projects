<?php

class Account_model extends CI_Model {
public function __construct() {
	$this->load->database();
	$this->userTable = 'users';
	$this->reviewTable = 'review';
}
public function get_info($username) {
	$this->db->select('*');
	$this->db->from($this->userTable);
	$this->db->where('username', $username);
	$query  = $this->db->get();
	return $query->row_array();
}

public function update($username, $firstname) {
	$data = array(
		'firstname'=>$firstname
	);
	$this->db->where('username', $username);
	$this->db->update('users', $data);
}

public function changePass($username, $newPass) {
	$data = array(
		'password'=>$newPass
	);
	$this->db->where('username', $username);
	$this->db->update('users', $data);
}

public function valid($username, $name) {
	$this->db->select('*');
	$this->db->from($this->userTable);
	$this->db->where('username', $username);
	$query  = $this->db->get();
		
		$row = $query->row_array();

		if (isset($row)) {
			return ($name == $row['firstname']);
		} else {
			return FALSE;
		}

}

public function upload($username, $filename) {
	$data = array(
		'profile'=>$filename
	);
	$this->db->where('username', $username);
	$this->db->update('users', $data);
}

public function add_comment($username, $rating, $title, $comment, $saller) {
    $data = array(
		'username' => $this->input->post('username'),
	    'rating' => $rating,
		'title' => $title,
		'comment' => $comment,
		'saller' => $saller
    );
    $this->db->insert(
        'review',
        $data
    );
}

public function reviews($saller) {
	$this->db->select('*');
	$this->db->from($this->reviewTable);
	$this->db->where('saller', $saller);
	$query  = $this->db->get();
	return $query->result();
}




}