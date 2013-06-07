<?php
               // Replace with real server API key from Google APIs  
                //$apiKey ="AIzaSyDmcELHVSAHmHGqUOhsC3qvC2q92y81WwQ";   //server key
				$apiKey   ="AIzaSyCwIlO9GRUvtevBVIrtMyIGKDbiwKjNOr4";    //browser key
                  // Replace with real client registration IDs
               $registrationIDs = array( "APA91bGDC0VKpcnPwjnYdjDZcfzH1SKQVdXa4NNFpuvO64Sc1mWVThkINZT63BrIUj_X2tr50c1EUnXikWV-N4VIUTYZxmbwtrmqH5HV5p39GgmbbrMivlhnEDZV8BO_DFX3UzU0Di9PV7k2f6rPBpl01M3Epdjyqw");

              // Message to be sent
             $message = "hi man";
			$message = array("price" => $message);
             // Set POST variables
            $url = 'https://android.googleapis.com/gcm/send';

           $fields = array(
           'registration_ids' => $registrationIDs,
             'data' =>  $message,
            );
         $headers = array(
          'Authorization: key=' . $apiKey,
         'Content-Type: application/json'
          );
			echo $message;
         // Open connection
              $ch = curl_init();

            // Set the url, number of POST vars, POST data
            curl_setopt( $ch, CURLOPT_URL, $url );
            curl_setopt( $ch, CURLOPT_POST, true );
            curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers);
                curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
             curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
               curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode( $fields ));
                // Execute post
             $result = curl_exec($ch);
echo $result;
echo $message;

            // Close connection
               curl_close($ch);
             echo $result;
              //print_r($result);
               //var_dump($result);
           ?>