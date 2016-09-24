package dev.cdc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dev.cdc.service.ClusteringEngine;
import dev.cdc.service.mkmpp.ClusterWrapper;
import dev.cdc.service.mkmpp.KMeansOptions;
import dev.cdc.service.mkmpp.KMeansPoint;

@Controller
@RequestMapping("/")
public class HelloController {

	private static final long WORLDWIDE_WOE = 1L;

	private static final long INDIA_WOE = 23424848L;

	private Twitter twitter;

	private ConnectionRepository connectionRepository;

	@Inject
	public HelloController(Twitter twitter, ConnectionRepository connectionRepository) {
		this.twitter = twitter;
		this.connectionRepository = connectionRepository;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String helloTwitter(Model model) {
		if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
			return "redirect:/connect/twitter";
		}
		model.addAttribute(twitter.userOperations().getUserProfile());
		model.addAttribute("friends", twitter.friendOperations().getFriends());
		model.addAttribute("followers", twitter.friendOperations().getFollowers());
		model.addAttribute("worldTrends", twitter.searchOperations().getLocalTrends(WORLDWIDE_WOE));
		model.addAttribute("indiaTrends", twitter.searchOperations().getLocalTrends(INDIA_WOE));
		List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline(50);
		System.out.println("hey");
		HashMap<String, String> textMap = new HashMap<String, String>(tweets.size());
		for (Tweet tweet : tweets) {
			textMap.put(tweet.getIdStr(), tweet.getText());
		}
		List<Tweet> tweets1 = new ArrayList<Tweet>(50);

		KMeansOptions kmeansOptions = new KMeansOptions(textMap, 10);
		List<ClusterWrapper> cwList = new ClusteringEngine().clusterKMeans(kmeansOptions);
		for (ClusterWrapper cw : cwList) {
			for (KMeansPoint p : cw.getChildren()) {
				tweets1.add(getTweet(p.getId(), tweets));
			}
		}
		model.addAttribute("tweets", tweets1);
		return "hello";
	}

	public Tweet getTweet(String id, List<Tweet> tweets) {
		for (Tweet tweet : tweets) {
			if (tweet.getIdStr().equals(id))
				return tweet;
		}
		return null;
	}
}
