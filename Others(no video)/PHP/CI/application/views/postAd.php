<div class="container">
<h2 class="text-center" id = "search-header">Post Ad</h2>
<hr>
<section id="addContent" >
    
<form method = "post" action="<?php echo base_url();?>add/new_item" enctype="multipart/form-data">
    <p>Title:</p>
    <input type = "text" name = "itemName" required>
    <p>Price:</p>
    <input name = "price" required>
    <p>Description:</p>
    <input type = "text" name = "description" required>
    <p>Select image to upload:</p>
    <input type="file" name="fileToUpload" id="fileToUpload">
    <input type="submit" value="POST AD" name="submit">
</form>
</section>
</div>
    



