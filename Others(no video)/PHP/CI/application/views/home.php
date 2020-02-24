<nav id="main-nav">
            <ul id="main-list">
                <li><a <?php echo 'href="'.base_url().'add/"'?> class = "dropbtn login-button">Post an ad</a></li>
                <li id="motor-drop">
                    <a class = "dropbtn" href="#">Motors</a>
                    <div class="drop-content">
                        <h3><a href = "#">View All Motors >></a></h3>
                        <ul>
                            <li><a href="#">Cars</a></li>
                            <li><a href="#">Motorbike & Scooters</a></li>
                            <li><a href="#">Vans</a></li>
                            <li><a href="#">Trucks</a></li>
                            <li><a href="#">Accessories</a></li>
                            <li><a href="#">Others</a></li>
                        </ul>
                    </div> 
                </li>
                <li id="bags-drop">
                    <a class = "dropbtn" href="#">Bags</a>
                    <div class="drop-content">
                        <h3><a href = "#">View All Bags >></a></h3>
                        <ul>
                            <li><a href="#">Shoulder Bag</a></li>
                            <li><a href="#">Totes</a></li>
                            <li><a href="#">Hobos</a></li>
                            <li><a href="#">Backpacks</a></li>
                            <li><a href="#">Messenger Bags</a></li>
                            <li><a href="#">Others</a></li>
                        </ul>
                    </div> 
                </li>
                <li id="furn-drop">
                    <a class = "dropbtn" href="#">furnitures</a>
                    <div class="drop-content">
                        <h3><a href = "#">View All Funitures >></a></h3>
                        <ul>
                            <li><a href="#">Bathroom</a></li>
                            <li><a href="#">Bedroom</a></li>
                            <li><a href="#">Cooking</a></li>
                            <li><a href="#">Living room</a></li>
                            <li><a href="#">Laundry</a></li>
                            <li><a href="#">Others</a></li>
                        </ul>
                    </div> 
                </li>
                <li id="pets-drop">
                    <a class = "dropbtn" href="#">Pets</a>
                    <div class="drop-content">
                        <h3><a href = "#">View All Pets >></a></h3>
                        <ul>
                            <li><a href="#">Cats</a></li>
                            <li><a href="#">Dogs</a></li>
                            <li><a href="#">Birds</a></li>
                            <li><a href="#">Fish & Aquarium</a></li>
                            <li><a href="#">Lost & Found Notice Board</a></li>
                            <li><a href="#">Others</a></li>
                        </ul>
                    </div>
                </li>
                <li id="electronic-drop">
                    <a class = "dropbtn" href="#">Electronic</a>
                    <div class="drop-content">
                        <h3><a href = "#">View All Electronics >></a></h3>
                        <ul>
                            <li><a href="#">Computers & Tablets</a></li>
                            <li><a href="#">Tvs</a></li>
                            <li><a href="#">headphones, Speakers & Audio</a></li>
                            <li><a href="#">Moblie phones</a></li>
                            <li><a href="#">Gaming</a></li>
                            <li><a href="#">Others</a></li>
                        </ul>
                    </div>
                </li>
                <li id="clothing-drop">
                    <a class = "dropbtn" href="#">Clothing</a>
                    <div class="drop-content">
                        <h3><a href = "#">View All Clothing >></a></h3>
                        <ul>
                            <li><a href="#">Tops</a></li>
                            <li><a href="#">Outerwear</a></li>
                            <li><a href="#">Jeans</a></li>
                            <li><a href="#">Swim</a></li>
                            <li><a href="#">Suiting</a></li>
                            <li><a href="#">Others</a></li>
                        </ul>
                    </div>
                </li>
                <li><a class = "dropbtn" href="#">Others</a></li>
            </ul>
        </nav>
    
        <div id="content">
            <section id="home-header">
                <figure class="container">
                    <figure class="img-container">
                        <?php echo '<a href="#"><img class="home-slides" src="'. base_url("./images/slides1.jpg"). '"></a>' ?>
                        <?php echo '<a href="#"><img class="home-slides" src="'. base_url("./images/slides2.jpg"). '"></a>' ?>
                        <?php echo '<a href="#"><img class="home-slides" src="'. base_url("./images/slides3.jpg"). '"></a>' ?>
                        <?php echo '<a href="#"><img class="home-slides" src="'. base_url("./images/slides4.jpg"). '"></a>' ?>
                        <?php echo '<a href="#"><img class="home-slides" src="'. base_url("./images/slides5.jpg"). '"></a>' ?>
                    </figure>
                    <div id="centered">
                         <a href="#top-picks" id="centered-text">#1 way to buy and sell fashion</a>
                    </div>
                        <!-- <a href="#top-picks" id="bottom">Shop Now</a> -->
                        <!-- <h2 id="centered">#1 way to buy and sell fashion</h2> -->
                </figure>
            </section>

            <section id="main-content">
                <article id="top-picks">
                    <h2>Top Picks</h2>
                    <div class="top-items">
                        <button class="top-before" onclick="plusDivs(-1)">&#10094;</button>
                        <figure class="item">
                        <?php echo '<a href="'.base_url().'item/"><img src="'. base_url("./images/phone.jpg"). '"></a>' ?>
                            <article class="item-description">
                                <h3>iPhoneX Silver 64g</h3>
                                <p>iPhone X silver 64gb 
                                        Unlocked to any network </p>
                            </article>
                        </figure>
                        <figure class="item">
                        <form <?php echo ' method = "post" action = "'.base_url().'item/load" >
                        <input class = "itemID" name = "itemID" value = "5"><img src="'. base_url("./images/sofa.jpg"). '"'?>> 
                            <article class="item-description">
                                <input class = "itemBtn" type = "submit" value = "Next large 3 setter sofa">
                                <p>Next large 3 setter sofa 
                                        Been professional cleaned </p>
                            </article>
                        </form>
                        </figure>
                        <figure class="item">
                        <?php echo '<a href="#"><img src="'. base_url("./images/car.jpg"). '"></a>' ?>
                            <article class="item-description">
                                <h3>This items Name</h3>
                                <p>There is a short item description, which is written by seller</p>
                            </article>
                        </figure>
                        <button class="top-next" onclick="plusDivs(1)">&#10095;</button>
                    </div>
                    <div class="top-items">
                            <button class="top-before" onclick="plusDivs(-1)">&#10094;</button>
                            <figure class="item">
                                <?php echo '<a href="#"><img src="'. base_url("./images/macbook.jpg"). '"></a>' ?>
                                <article class="item-description">
                                    <h3>MacBook Pro 13"8GB 2017</h3>
                                    <p>Only had this a month but upgraded to a desktop for work purposes. </p>
                                </article>
                            </figure>
                            <figure class="item">
                                <?php echo '<a href="#"><img src="'. base_url("./images/bag.jpg"). '"></a>' ?>
                                <article class="item-description">
                                    <h3>Classic man bag</h3>
                                    <p>It is in very good condition and can fit an iPad in it</p>
                                </article>
                            </figure>
                            <figure class="item">
                                <?php echo '<a href="#"><img src="'. base_url("./images/kindle.jpg"). '"></a>' ?>
                                <article class="item-description">
                                    <h3>Kindle paperwhite 4gb 3g <wi-fi></wi-fi></h3>
                                    <p>Brand new still wrapped white kindle new series 7 with touch screen.</p>
                                </article>
                            </figure>
                            <button class="top-next" onclick="plusDivs(1)">&#10095;</button>
                        </div> 
                </article>

                
                <article id="for-you">
                    <h2>For You</h2>
                    <div class="for-you-items">
                        <button class="for-you-before" onclick="plusDivs1(-1)">&#10094;</button>
                        <figure class="item">
                            <?php echo '<a href="#"><img src="'. base_url("./images/macbook.jpg"). '"></a>' ?>
                            <article class="item-description">
                                <h3>MacBook Pro 13"8GB 2017</h3>
                                <p>Only had this a month but upgraded to a desktop for work purposes. </p>
                            </article>
                        </figure>
                        <figure class="item">
                            <?php echo '<a href="#"><img src="'. base_url("./images/sofa.jpg"). '"></a>' ?>
                            <article class="item-description">
                                <h3>Next large 3 setter sofa</h3>
                                <p>Next large 3 setter sofa 
                                        Been professional cleaned </p>
                            </article>
                        </figure>
                        <figure class="item">
                            <?php echo '<a href="#"><img src="'. base_url("./images/bag.jpg"). '"></a>' ?>
                            <article class="item-description">
                                <h3>Classic man bag</h3>
                                <p>It is in very good condition and can fit an iPad in it</p>
                            </article>
                        </figure>
                        <button class="for-you-next" onclick="plusDivs1(1)">&#10095;</button>
                    </div>
                    <div class="for-you-items">
                            <button class="for-you-before" onclick="plusDivs1(-1)">&#10094;</button>
                            <figure class="item">
                                <?php echo '<a href="#"><img src="'. base_url("./images/phone.jpg"). '"></a>' ?>
                                <article class="item-description">
                                    <h3>iPhoneX Silver 64g</h3>
                                    <p>iPhone X silver 64gb 
                                            Unlocked to any network </p>
                                </article>
                             </figure>
                            <figure class="item">
                                <?php echo '<a href="#"><img src="'. base_url("./images/car.jpg"). '"></a>' ?>
                                <article class="item-description">
                                    <h3>This items Name</h3>
                                    <p>There is a short item description, which is written by seller</p>
                                </article>
                            </figure>
                            <figure class="item">
                                <?php echo '<a href="#"><img src="'. base_url("./images/kindle.jpg"). '"></a>' ?>
                                <article class="item-description">
                                    <h3>Kindle paperwhite 4gb 3g <wi-fi></wi-fi></h3>
                                    <p>Brand new still wrapped white kindle new series 7 with touch screen.</p>
                                </article>
                            </figure>
                            <button class="for-you-next" onclick="plusDivs1(1)">&#10095;</button>
                        </div> 
                </article>
            </section>
        </div>