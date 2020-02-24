<div class="container">
    
<h2 class="text-center" id = "search-header">Search result</h2>
    <table id = "search-table">
    
        <thead>
        <tr>
            <th>Name</th>
            <th>Price</th>
            <th>image</th>
        </tr>
        <hr>
        </thead>
        
        <tbody>
        <?php 
		if(isset($item))
		foreach($item as $row){
			echo '
                <tr>
                    <form method = "post" action = "'.base_url().'item/load">
                    <input class = "itemID" name = "itemID" value = "'.$row->itemID.'">
                    <td><input type = "submit" name = "submit" class = "itemBtn item-link" value = "'.$row->itemName.'"></td>
                    <td><input type = "submit" name = "submit" class = "itemBtn item-link" value = "'.$row->price.'"></td>
                    <td><img src="'. base_url($row->image). '"></td>
                    </form>
				</tr>
			';
		}

		?>		
        </tbody>
    </table>
</div>