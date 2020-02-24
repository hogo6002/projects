<?php
class Item_model extends CI_Model {

	public function __construct() {
		$this->load->database();
		$this->userTable = 'users';
		$this->itemTable = 'item';
		$this->wishlistTable = 'wishlist';
	}
    
	public function exist($itemID) {
		$this->db->select('*');
		$this->db->from($this->itemTable);
		$this->db->where('itemID', $itemID);
		$query  = $this->db->get();
		$row = $query->row_array();

		if (isset($row)) {
			return TRUE;
		} else {
			return FALSE;
		}

	}

	public function get_info($itemID) {
		$this->db->select('*');
		$this->db->from($this->itemTable);
		$this->db->where('itemID', $itemID);
		$query  = $this->db->get();
		$row = $query->row_array();
		return $row;
	}

	public function add_item($itemID, $itemName, $image, $price, $saller, $description) {
		$data = array(
			'itemID' => $itemID,
			'itemName' => $itemName,
			'image' => $image,
			'price' => $price,
			'saller' => $saller,
			'description' => $description
		);
		$this->db->insert(
			'item',
			$data
		);
	}

	public function new_item() {
		$query = $this->db->query("SELECT * FROM item WHERE itemID=(SELECT MAX(itemID) FROM item);");
		$row = $query->row_array();
		return $row;
	}

	public function search($itemName) {
		$this->db->select('*');
		$this->db->from($this->itemTable);
		$this->db->like("itemName", $itemName);
        $query  = $this->db->get();
		return $query->result();
	}

	public function get_items($username) {
		$this->db->select('*');
		$this->db->from($this->userTable);
		$this->db->where("username", $username);
		$query4  = $this->db->get();
		$this->db->select('*');
		$this->db->from($this->wishlistTable);
		$this->db->where("username", $username);
		$query  = $this->db->get();
		// $query = $this->db->query("SELECT * FROM wishlist WHERE username = '" . $username . "'");
		// $query4 = $this->db->query("SELECT * FROM users WHERE username = '" . $username . "'");
		$query3 = $this->get_item(0, $query);
		for ($x = 1; $x < $query4->row_array()['wishlist']; $x++) {
			// $query3 = array_merge($query3, $this->get_item($x, $query));
		}
		return $query3;
	}

	public function get_item($index, $query) {
		$i = 0;
		foreach ($query->result() as $row)
		{
			if ($i==$index) {
				$this->db->select('*');
				$this->db->from($this->itemTable);
				$this->db->where("itemID", $row->itemID);
				$query  = $this->db->get();
				return $query->result();
			}
			$i++;
		}
	}

	public function add_wishlist($username, $itemID) {
		$data = array(
			'username' => $username,
			'itemID' => $itemID
		);
		$this->db->insert(
			'wishlist',
			$data
		);
		$query = $this->db->query("UPDATE users set wishlist = wishlist+1 WHERE username = '" . $username . "'");
	}

	public function check_wishlist($username, $itemID) {
		$this->db->select('*');
		$this->db->from($this->wishlistTable);
		$this->db->where("username", $username);
		$this->db->where("itemID", $itemID);
		$query  = $this->db->get();
		$row = $query->row_array();
		if (isset($row)) {
			return FALSE;
		} else {
			return TRUE;
		}
	}

	public function remove_wishlist($username, $itemID) {
		$data = array(
			'username' => $username,
			'itemID' => $itemID
		);
		$this->db->delete(
			'wishlist',
			$data
		);
		$query = $this->db->query("UPDATE users set wishlist = wishlist-1 WHERE username = '" . $username . "'");
	}

	public function GetRow($keyword) {       
        $this->db->like("itemName", $keyword);
        return $this->db->get('item')->result_array();
    }

	
}