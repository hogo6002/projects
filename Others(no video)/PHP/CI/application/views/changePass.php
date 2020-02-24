<form method='post' action = "<?php echo base_url();?>password/update">
                    <td>Email:</td>                  
                    <td><input name='username' type='text' required='required'/></td>
                    <td>First Name:</td>                  
                    <td><input name='name' type='text' required='required'/></td>
                <tr>
                    <td>New Password:</td>
                    <td><input name='newpw' type='password' required = 'required' /></td>
                <tr>                    
                    <td>Confirm Password:</td>                  
                    <td><input name='conpw' type='password' required = 'required' /></td>
                    <td> 
                    <input type='submit' value='Change Password' />
                    </td>
                </tr>           
                 </form>
    

