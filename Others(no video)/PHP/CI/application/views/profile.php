

<section id="profile-content">
    <h2>My account</h2> 
        <article id = "profile-detail">
            <h3>
                <article id="name-form">
                <?php echo ucfirst($firstname) . " " . ucfirst($lastname);
                ?>
                <form method='post' action = "<?php echo base_url();?>profile/update">
                    <input name='firstname' type='text'/>
                    <input type='submit' value='Change Name' id="change-name"/>
                </form> 
                </article>
                <hr>
            </h3>
            <h4>
                <?php echo "Login email: " . $username;
                ?>
            </h4>  
            <hr>
        </article>
        <article>
            <?php echo '<a href="#"><img id = "profile-img" src="'. base_url($profile). '"></a>' ?>
            <form action="<?php echo base_url();?>profile/do_upload" method="post" enctype="multipart/form-data">
                Select image to upload:
                <input type="file" name="fileToUpload" id="fileToUpload">
                <input type="submit" value="Upload Image" name="submit">
            </form>
        </article>
        <div id="basicMap"></div>
        <script src="<?php echo base_url('js/OpenLayersjs');?>"></script>
    <script>
      function init() {
        map = new OpenLayers.Map("basicMap");
        var mapnik         = new OpenLayers.Layer.OSM();
        var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
        var toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
        var position       = new OpenLayers.LonLat(13.41,52.52).transform( fromProjection, toProjection);
        var zoom           = 15; 
        map.addLayer(mapnik);
        map.setCenter(position, zoom );
      }
    </script>

</section>
    

