

<section class="center" id="profile">
    <h2>个人中心</h2> 
        <article id = "profile-detail">
            <h3>你好，<?php echo ucfirst($name);?></h3>
        </article>

        <ul>
            <?php foreach ($orderArray as $key=>$value) {
                    $orderInfo = explode("||", $value);?>
                    <li>
                        <h3><?php echo $orderInfo[0]?></h3> 
                        <h3><?php echo $orderInfo[1]?></h3> 
                        <h3><?php echo $orderInfo[2]?></h3> 
                        <h3>¥ <?php echo $orderInfo[3]?>.00/日</h3>
                    </li>
                <?php }?>   
            </ul> 

</section>
    

