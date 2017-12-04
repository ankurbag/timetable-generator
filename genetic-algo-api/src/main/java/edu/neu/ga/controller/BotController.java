package edu.neu.ga.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.neu.ga.response.BotResponse;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import io.swagger.annotations.Api;

@Controller
@Api(value = "TimeTableBot", description = "Bot Controller")
@RequestMapping("/timetable-bot")
public class BotController {

	public static String API_AI_TOKEN = "4772119c66424ab984f4915ff6ac2e11";
	
	/**
	 * Invokes BOT.
	 * @param textinput
	 * @return
	 */
	@RequestMapping(value = "/{textinput}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public BotResponse askBot(@PathVariable("textinput") String textinput) {
		AIConfiguration configuration = new AIConfiguration(API_AI_TOKEN);
		AIDataService dataService = new AIDataService(configuration);
		BotResponse botresponse = new BotResponse();
		HashMap params = new HashMap();

		try {
			AIRequest request = new AIRequest(textinput);

			AIResponse aiResponse = dataService.request(request);

			if (aiResponse.getStatus().getCode() == 200) {

				String intentName = "";
				String aiResponseText = "";
				String input = "";
				if (aiResponse.getResult() != null && aiResponse.getResult().getMetadata() != null &&
						aiResponse.getResult().getMetadata().getIntentName() != null)
					intentName = aiResponse.getResult().getMetadata().getIntentName();

				////////////////////
				/*Museum museum = null;

				switch (intentName) {
				case "GetMuseumLocation":
					museum = getMuseum(params, aiResponse, input, museum);
					aiResponseText = "The museum " + input + " is located at " + museum.getStreet() + ", "
							+ museum.getCity() + ", " + museum.getState() + " " + museum.getZip();
					botresponse.setSpeech(aiResponseText);
					break;
				case "GetPrice":
					museum = getMuseum(params, aiResponse, input, museum);
					museum = ConnectionDao.getMuseumPrice(ConnectionDao.getConnection(), museum.getId());
					botresponse.setSpeech(museum.getPriceDetails());
					break;
				case "GetVisitingHours":
					museum = getMuseum(params, aiResponse, input, museum);
					museum = ConnectionDao.getMuseumTimings(ConnectionDao.getConnection(), museum.getId());
					botresponse.setSpeech(museum.getTimingDetails());
					break;
				default:
					botresponse.setSpeech(aiResponse.getResult().getFulfillment().getSpeech());
					break;
				}*/
				//////////////////////

			} else {
				botresponse.setSpeech(aiResponse.getStatus().getErrorDetails());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return botresponse;
	}

}
