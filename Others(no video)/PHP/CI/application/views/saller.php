

        <section id="profile-content">
        
        <h2>
            <?php echo ucfirst($user['firstname']);
            ?>
        </h2>
        
        <?php echo '<a href="#"><img id = "profile-img" src="'. base_url($user['profile']). '"></a>' ?>

        <form method = "post" action="<?php echo base_url();?>Saller/add_comment" enctype="multipart/form-data">
            <?php echo '<input name = "username" value = "'.$user['username'].'" class = "itemID">';?>
            <label>Rating: </label>
            <input type = "number" name = "rating" required>
            <label>Title: </label>
            <input type = "text" name = "title" required>
            <label>Comment: </label>
            <input type = "text" name = "comment" required>
            <input type="submit" value="POST AD" name="submit">
        </form>

        <?php 
            if(isset($review))
            foreach($review as $row){
                echo '
                <table>
                    <tr>
                        <td>'.$row->rating.'</td>
                        <td>'.$row->title.'</td>
                        <td>'.$row->username.'</td>
                    </tr>
                    </table>
                ';
            }
        ?>


        </section>
    

