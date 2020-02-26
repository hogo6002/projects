        <div id="main-content">
            <section id="home-header" class="show-phone">
                <figure class="container">
                    <figure class="img-container">
                        <a href="#"><img class="home-slides" src="<?php echo base_url('images/bg1.jpg');?>"></a>
                        <a href="#"> <img class="home-slides" src="<?php echo base_url('images/bg2.jpg');?>"></a>
                    </figure>
                </figure>
            </section>
            <form id="book-form" method = "GET" action = "<?php echo base_url();?>index.php/car/search">
                <h2>车辆预约</h2>
                <figure class="label-container">
                    <label for="pick-up-date">取车时间</label> 
                </figure>
                <br>
                <?php $now=date("Y-m-d");
                      $max=date("Y-m-d",mktime(0, 0, 0, date("m")+1, date("d"), date("Y")));
                      $max2=date("Y-m-d",mktime(0, 0, 0, date("m")+2, date("d"), date("Y")));
                ?>
                <input type="date" name="pick-up-date" class="date-input" min="<?php echo $now?>" max="<?php echo $max?>" value="<?php echo $now?>" required>
                <input type="time" name="pick-up-time" class="time-input" value = "09:00:00" min="07:00:00" max="23:00:00" required>
                <br>
                <figure class="label-container">
                    <label for="drop-off-date">退车时间</label> 
                </figure>
                <br>
                <input type="date" name="drop-off-date" class="date-input" min="<?php echo $now?>" max="<?php echo $max2?>" value="<?php echo $max?>" required>
                <input type="time" name="drop-off-time" class="time-input" value = "09:00:00" min="07:00:00" max="23:00:00" required>
                <br>
                <figure class="submit-container">
                    <input type="submit" name="submit" value="立即选车" id="submit"> 
                </figure>
                
            </form>
        </div>