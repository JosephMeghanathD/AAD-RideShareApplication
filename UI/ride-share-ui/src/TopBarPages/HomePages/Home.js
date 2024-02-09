import HomePageText from "./HomePage";
import RideList from "./RidesData/RideList";

const Home = () => {

	const data = {
		"rides": [
		  {
			"rideId": "1",
			"startingFromLocation": "123 Main St",
			"destination": "456 Elm St",
			"numberOfPeople": 2,
			"fare": 10.50,
			"timeOfRide": 1644325200,
			"postedBy": "user123",
			"postedAt": 1644324600
		  },
		  {
			"rideId": "2",
			"startingFromLocation": "789 Oak St",
			"destination": "101 Pine St",
			"numberOfPeople": 1,
			"fare": 8.75,
			"timeOfRide": 1644408000,
			"postedBy": "user456",
			"postedAt": 1644407400
		  },
		  {
			"rideId": "3",
			"startingFromLocation": "222 Maple St",
			"destination": "333 Birch St",
			"numberOfPeople": 3,
			"fare": 15.20,
			"timeOfRide": 1644490800,
			"postedBy": "user789",
			"postedAt": 1644490200
		  },
		  {
			"rideId": "4",
			"startingFromLocation": "444 Cedar St",
			"destination": "555 Walnut St",
			"numberOfPeople": 1,
			"fare": 6.90,
			"timeOfRide": 1644577200,
			"postedBy": "user101",
			"postedAt": 1644576600
		  },
		  {
			"rideId": "5",
			"startingFromLocation": "666 Pine St",
			"destination": "777 Oak St",
			"numberOfPeople": 2,
			"fare": 12.25,
			"timeOfRide": 1644663600,
			"postedBy": "user111",
			"postedAt": 1644663000
		  },
		  {
			"rideId": "6",
			"startingFromLocation": "888 Elm St",
			"destination": "999 Main St",
			"numberOfPeople": 1,
			"fare": 9.60,
			"timeOfRide": 1644750000,
			"postedBy": "user222",
			"postedAt": 1644749400
		  },
		  {
			"rideId": "7",
			"startingFromLocation": "121 Oak St",
			"destination": "232 Pine St",
			"numberOfPeople": 4,
			"fare": 20.75,
			"timeOfRide": 1644836400,
			"postedBy": "user333",
			"postedAt": 1644835800
		  },
		  {
			"rideId": "8",
			"startingFromLocation": "343 Birch St",
			"destination": "454 Maple St",
			"numberOfPeople": 1,
			"fare": 7.80,
			"timeOfRide": 1644922800,
			"postedBy": "user444",
			"postedAt": 1644922200
		  },
		  {
			"rideId": "9",
			"startingFromLocation": "565 Walnut St",
			"destination": "676 Cedar St",
			"numberOfPeople": 2,
			"fare": 13.45,
			"timeOfRide": 1645009200,
			"postedBy": "user555",
			"postedAt": 1645008600
		  },
		  {
			"rideId": "10",
			"startingFromLocation": "787 Main St",
			"destination": "898 Elm St",
			"numberOfPeople": 1,
			"fare": 8.90,
			"timeOfRide": 1645095600,
			"postedBy": "user666",
			"postedAt": 1645095000
		  }
		]
	  };
	return (
		<div>
			<HomePageText/>
			<RideList rides={data.rides} />
		</div>
	);
};

export default Home;
