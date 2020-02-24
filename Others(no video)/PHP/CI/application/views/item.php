<section id="itemContent">
    <figure id="itemImg">
        <?php echo '<a href="#"><img src="'. base_url($image). '"></a>' ?>
    </figure>
    <section id="itemInfo">
        <h2><?php echo $itemName;
            ?></h2>
        <h3><?php echo "$" . $price . ".00";
            ?></h3>
            <?php echo '
            <form method = "post" action = "'.base_url().'saller">'?>
            <h3><?php echo "Saller: " . $saller;?></h3>
            <input class = "itemID" name = 'username' value = "<?php echo $saller;?>">
            <input type = "submit" value = "About the seller">
            </form>
            
        <p><?php echo $description;
            ?></p>
    </section>
    <?php
    echo '
    <form method = "post" action = "'.base_url().'wishlist/add_item">
        <input type = "text" value = "'.$itemID.'" name = "itemID" class = "itemID">
        ';
        if ($exist) {
            echo '<input type="submit" value = "add to wishlist"> ';
        } else {
        }     
    echo' </form>';
    echo '
    <form method = "post" action = "'.base_url().'stripeController">
        <input type="submit" value = "pay it">
        <input type = "text" value = "'.$price.'" name = "price" class = "itemID">
    </form>'

    ?>
   
</section>
    



