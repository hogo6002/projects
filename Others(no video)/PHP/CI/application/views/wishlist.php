<div class="container">
<h2 class="text-center" id = "search-header">Wishlist</h2>
    
    <table id="search-table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Price</th>
            <th>image</th>
        </tr>
        </thead>
        <hr>
        <tbody>
        <?php 
        echo '
            <tr>
                <td> '.$user['firstname'].'</td>
            </tr>    
        ';
		if(isset($item))
		foreach($item as $row){
            echo '
                <tr>
                
                    <form method = "post" action = "'.base_url().'item/load">
                    <input class = "itemID" name = "itemID" value = "'.$row->itemID.'">
                    <td><input type = "submit" name = "submit" class = "itemBtn item-link" value = "'.$row->itemName.'"></td>
                    <td><input type = "submit" name = "submit" class = "itemBtn item-link" value = "'.$row->price.'"></td>
                    </form>
                    <td><img src="'. base_url($row->image). '"><td>
                    <form method = "post" action = "'.base_url().'wishlist/remove_item">
                <input class = "itemID" name = "itemID" value = "'.$row->itemID.'">
                <td><input type = "submit" name = "submit" value = "remove from wishlist"><td>
                    </form>
                    
				</tr>
			';
		}

		?>		
        </tbody>
        
    </table>
</div>