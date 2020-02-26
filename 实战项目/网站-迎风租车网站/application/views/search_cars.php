        <div id="cars-container">
            <h2>选择车辆</h2>
            <ul>
                <?php foreach ($carArray as $key=>$value) {
                    $carInfo = explode("||", $value);?>
                    <li class="car-detail">
                        <figure class="car-img">    
                            <img src="<?php echo base_url("$carInfo[2]")?>">
                        </figure>
                         <h3>¥ <?php echo $carInfo[1]?>.00</h3>   
                        <form action="<?php echo base_url();?>index.php/car/detail" method = "GET">
                            <input class="not-show" type="text" name="carId" value="<?php echo $carInfo[3]?>">
                            <input class="not-show" type="text" name="startTime" value="<?php echo $startTime?>">
                            <input class="not-show" type="text" name="endTime" value="<?php echo $endTime?>">
                            <input class="not-show" name="status" value="no">
                            <input class="submit-btn" type="submit" name="" value = "<?php echo $carInfo[0]?>">
                        </form>
                    </li>
                <?php }?>   
            </ul>   
        </div>