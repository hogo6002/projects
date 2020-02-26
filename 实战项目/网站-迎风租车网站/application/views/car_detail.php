
		<div id="cars-container" class="center">	
			<div class="car-detail">
				<h3 class="car-detail-p"><?php echo $carArray['carName']?></h3>
				<strong class="car-detail-p">¥ <?php echo $carArray['price']?>.00起/日均</strong>
			</div>
			<figure class="car-img-detial">
				<img src="<?php echo base_url($carArray['image'])?>">
			</figure>
			<form action="<?php echo base_url();?>index.php/car/submit" method = "GET" action="order.php" 
				<?php if($status == "yes") {
					echo 'class="not-show"';}?>
				>
					<input class="not-show" type="text" name="carId" value="<?php echo $id?>">
	                <input class="not-show" type="text" name="startTime" value="<?php echo $startTime?>">
	                <input class="not-show" type="text" name="endTime" value="<?php echo $endTime?>">
	                <input class="not-show" type="number" name="price" value="<?php echo $carArray['price']?>">
	                <input class="order-btn" type="submit" value="立即预订">
				</form>
		</div>