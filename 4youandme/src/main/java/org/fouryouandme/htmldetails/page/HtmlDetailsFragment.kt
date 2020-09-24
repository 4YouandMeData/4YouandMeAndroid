package org.fouryouandme.htmldetails.page

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.html_detail.*
import org.fouryouandme.R
import org.fouryouandme.auth.integration.App
import org.fouryouandme.auth.optin.permission.OptInPermissionFragmentArgs
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*
import org.fouryouandme.htmldetails.HtmlDetailsStateUpdate

class HtmlDetailsFragment : BaseFragment<HtmlDetailsViewModel>(R.layout.html_detail) {

    private val args: HtmlDetailsFragmentArgs by navArgs()

    override val viewModel: HtmlDetailsViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                HtmlDetailsViewModel(
                    navigator,
                    IORuntime,
                    injector.configurationModule()
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is HtmlDetailsStateUpdate.Initialization -> applyConfiguration(
                        it.configuration,
                        args.pageId
                    )
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {
            if (viewModel.isInitialized().not()) {
                viewModel.initialize()
            }

            evalOnMain { applyConfiguration(viewModel.state().configuration, args.pageId) }
        }
    }

    private fun applyConfiguration(configuration: Configuration, pageId: Int) {

        setStatusBar(configuration.theme.primaryColorStart.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        detailsToolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())

        backArrow.setImageResource(imageConfiguration.back())
        backArrow.setOnClickListener {
            viewModel.back(findNavController())
        }

        title.setTextColor(configuration.theme.secondaryColor.color())

        setupWebView(pageId)
    }

    private fun setupWebView(pageId: Int) {

        //TODO: popolare questo contenuto con l'HTML proveniente dal backend
        var source = ""

        when (pageId) {
            0 -> {
                title.text = "Contact information"
                source =
                    "<!DOCTYPE html>\n<html>\n<head><meta charset= \"utf-8\">\n<title> Stress in Crohn\'s Research Team </title>\n<meta name=\"description\" content=\"\">\n<meta name=\"author\" content=\"\">\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n<style>\n  body\n  {\n    font-family: \"Helvetica\", \"Arial\", sans-serif;\n  }\n  a:link {\n    color:#144E30;\n  }\n  a:visited{\n    color:#144E30\n  }\n.accordion {\n  background-color: #eee;\n  color: #444;\n  cursor: pointer;\n  padding: 18px;\n  width: 100%;\n  border: none;\n  text-align: left;\n  outline: none;\n  font-size: 15px;\n  transition: 0.4s;\n}\n\n.active, .accordion:hover {\n  background-color: #5ABB8B;\n}\n\n.panel {\n  padding: 0 18px;\n  display: none;\n  background-color: white;\n  overflow: hidden;\n}\n</style>\n</head>\n<body>\n  <h2 style=\"color:#5ABB8B\">Contact Information</h2>\n  <button class=\"accordion\"> Stress in Crohn\'s Research Team- Mount Sinai</button>\n  <div class=\"panel\">\n  <h2 style=\"color:#5ABB8B\"> Anthony Biello </h2>\n  \t<p>\n  \t\t<i> Clincial Research Coordinator</i>, Stress in Crohn\'s Study\n  \t\t<br>\n  \t\tIcahn School of Medicine at Mt. Sinai\n  \t\t<br> <br>\n  \t\t<a href=\"mailto:anthony.biello@mountsinai.org?Subject=Crohn&#8217s%20Help\" target=\"_top\"> anthony.biello@mountsinai.org</a>\n  \t\t<br>\n  \t\t<a href=\"tel+12128247736\">212-824-7736</a>\n  \t</p>\n\t<h2 style= \"color:#5ABB8B\">Bruce Sands M.D.</h2>\n\t\t<p>\n\t\t\t<i>Principal Investigator</i>, Stress in Crohn\'s Study\n\t\t\t<br>\n\t\t\tDivision of Gastroenterology\n\t\t\t<br>\n\t\t\tIcahn School of Medicine at Mt. Sinai\n\t\t\t<br> <br>\n\t\t\t<a href=\"mailto:bruce.sands@mssm.edu?Subject=Crohn&#8217s%20Help\" target=\"_top\">bruce.sands@mssm.edu</a>\n\t\t\t<br>\n\t\t</p>\n  </div>\n  <button class=\"accordion\"> Stress in Crohn\'s Research Team- Oxford</button>\n  <div class=\"panel\">\n    <h2 style=\"color:#5ABB8B\"> Gastroenterology Clinical Trials Facility, John Radcliffe Hospital </h2>\n    \t<p>\n    \t\t<a href=\"mailto:CTF.ResearchNurses@nhs.net\" target=\"_top\">CTF.ResearchNurses@nhs.net</a>\n    \t\t<br>\n    \t\t<a href=\"tel+4401865231619\">01865 231619</a>\n    \t</p>\n      <h2 style= \"color:#5ABB8B\">Alissa Walsh M.D.</h2>\n    \t\t<p>\n    \t\t\t<i>Chief Investigator</i>, Stress in Crohn\'s Study\n    \t\t\t<br>\n    \t\t\tInflammatory Bowel Disease Service\n    \t\t\t<br>\n    \t\t\tTranslational Gastroenterology Department\n          <br>\n          John Radcliffe Hospital, Oxford\n    \t\t\t<br> <br>\n    \t\t\t<a href=\"mailto:alissa.walsh@ouh.nhs.uk\" target=\"_top\">alissa.walsh@ouh.nhs.uk</a>\n    \t\t\t<br>\n    \t\t</p>\n  </div>\n  <script>\n  var acc = document.getElementsByClassName(\"accordion\");\n  var i;\n\n  for (i = 0; i < acc.length; i++) {\n    acc[i].addEventListener(\"click\", function() {\n      this.classList.toggle(\"active\");\n      var panel = this.nextElementSibling;\n      if (panel.style.display === \"block\") {\n        panel.style.display = \"none\";\n      } else {\n        panel.style.display = \"block\";\n      }\n    });\n  }\n  </script>\n\n  </body>\n  </html>\n"
            }
            1 -> {
                title.text = "Rewards"
                source =
                    "<html>\n<head>\n  <meta charset=\"utf-8\">\n  <title>Points and Compensation</title>\n  <meta name=\"description\" content=\"\">\n  <meta name=\"author\" content=\"\">\n  <!-- Mobile Specific Metas\n  –––––––––––––––––––––––––––––––––––––––––––––––––– -->\n  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n</head>\n<body>\n  <h2 style=\"color:#5ABB8B\"> Points System and Compensation</h2>\n  <p> You\'ll receive points for wearing sensors, taking surveys, and completing other study tasks. You can earn a gift card each month by reaching a fixed number of points.</p>\n  <p> Points may take up to 24 hours to appear in your Activity Feed.</p>\n\n<style>\n  body\n{\n    font-family: \"Helvetica\", \"Arial\", sans-serif;\n  }\ntable, th, td {\nborder: 1px solid black;\nborder-collapse: collapse;\n}\nth, td {\n  padding:10px;\n}\nth\n{\n  text-align: left;\n}\n</style>\n  <table>\n  <tr>\n    <th style=\"color: #5ABB8B\"> Frequency </th>\n    <th style=\"color: #5ABB8B\"> Activity </th>\n    <th style=\"color: #5ABB8B\"> Points </th>\n  </tr>\n   <td> Daily tasks </td>\n   <td> </td>\n   <td> </td>\n <tr>\n   <td> </td>\n   <td> Self-assessed mood question </td>\n   <td> 5 </td>\n </tr>\n <tr>\n   <td> </td>\n   <td> Survey </td>\n   <td> 5 </td>\n </tr>\n <tr>\n   <td> </td>\n   <td>Trail-making task/Reaction-time task</td>\n   <td> 10 </td>\n </tr>\n <tr>\n   <td> </td>\n   <td> Wear Empatica (x2 boost for 3 days in a row)* </td>\n   <td> 5 </td>\n </tr>\n <tr>\n   <td> </td>\n   <td> Wear Oura (x2 boost for 3 days in a row)* </td>\n   <td> 5 </td>\n </tr>\n <tr>\n   <td> </td>\n   <td> Bodyport weigh-in (x2 boost for 3 days in a row)* </td>\n   <td> 5 </td>\n </tr>\n <tr>\n   <td>Bi-weekly tasks </td>\n   <td> </td>\n   <td> </td>\n </tr>\n <tr>\n   <td> </td>\n   <td> 6-minute walk test </td>\n   <td> 30 </td>\n </tr>\n <tr>\n   <td> </td>\n   <td> Video diary </td>\n   <td> 30 </td>\n </tr>\n </table>\n<p> (*): 4x boost for all 3 devices 3 days in a row </p>\n<h4 style=\"color: #144E30\">Gift Card Amounts </h4>\n<table>\n <style>\n table, th, td {\n   border: 1px solid black;\n   border-collapse: collapse;\n }\n th, td {\n   padding:10px;\n }\n th\n {\n   text-align: left;\n }\n </style>\n   <tr>\n     <th style=\"color: #5ABB8B\"> Monthly points awarded </th>\n     <th style=\"color: #5ABB8B\"> Gift card value </th>\n   </tr>\n   <tr>\n     <td> >2000 </td>\n     <td> $40 (£30) </td>\n   </tr>\n   <tr>\n     <td> 1500-2000 </td>\n     <td> $30 (£23) </td>\n   </tr>\n   <tr>\n     <td> 1000-1500 </td>\n     <td> $20 (£15) </td>\n   </tr>\n   <tr>\n     <td> 500-1000 </td>\n     <td> $15 (£12) </td>\n   </tr>\n </table>\n</body>\n</html>\n"
            }
            2 -> {
                title.text = "Rewards"
                source =
                    "<!DOCTYPE html>\n<html>\n<head>\n<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n<style>\n  body\n  {\n    font-family: \"Helvetica\", \"Arial\", sans-serif;\n  }\n.accordion {\n  background-color: #eee;\n  color: #444;\n  cursor: pointer;\n  padding: 18px;\n  width: 100%;\n  border: none;\n  text-align: left;\n  outline: none;\n  font-size: 15px;\n  transition: 0.4s;\n}\n\n.active, .accordion:hover {\n  background-color: #5ABB8B;\n}\n\n.panel {\n  padding: 0 18px;\n  display: none;\n  background-color: white;\n  overflow: hidden;\n}\n</style>\n</head>\n<body>\n\n<h2 style=\"color:#5ABB8B\">Frequently Asked Questions</h2>\n<h3> Oura Ring </h3>\n<button class=\"accordion\">How do I charge my Oura ring?</button>\n<div class=\"panel\">\n  <p>Place the ring on the charger provided to you. The charger size should match the size of the ring you are wearing. Let the device charge fully for optimal battery performance. This can take between 20-80 minutes. We suggest charging your device when you do your morning routine or when taking a shower.</p>\n</div>\n\n<button class=\"accordion\">How do I sync my Oura Ring?</button>\n<div class=\"panel\">\n  <p>Your ring will sync with the app through Bluetooth connection. You will set up this connection when you set up the device. If your app is unable to find your ring while the ring is on your finger, connect the ring to the charger, keep your phone close to the ring, and try again. For more help, please refer to your Participant Guide.</p>\n</div>\n<button class=\"accordion\">How do I sync my Oura Ring data?</button>\n<div class=\"panel\">\n  <p>Open the Oura App to sync the data from your ring. You will see your data in the Oura App, once the sync is completed. There is a delay of up to 24 hours for the data to show in the study app.</p>\n</div>\n<h3> Empatica Embrace Wristband </h3>\n<button class=\"accordion\">How do I charge my Empatica Embrace wristband?</button>\n<div class=\"panel\">\n  <p>      Place the device on the charger provided to you. Let the device charge fully for optimal battery performance. This can take about 1 hour and 40 minutes. You will need to charge your device every day. We suggest you charge your device at night when you sleep.</p>\n</div>\n<button class=\"accordion\">How do I sync my Empatica Embrace wristband?</button>\n<div class=\"panel\">\n  <p>   Embrace needs to be connected via Bluetooth to a dedicated iPhone that is always with you, and the Mate App needs to be open on your  mobile device. You can make sure data from the device is being synced, by always having the Mate App open.\n        For more help, please refer to your Participant Guide.</p>\n</div>\n<button class=\"accordion\">Why am I receiving notifications from my Empatica Embrace wristband?</button>\n<div class=\"panel\">\n  <p> The device is designed to alert users for different events. Learn more about these alerts and how to turn them off by referring to your Participant Guide.</p>\n</div>\n<h3>Bodyport Smart Scale</h3>\n<button class=\"accordion\">How do I turn on my scale?</button>\n<div class=\"panel\">\n  <p>Your scale will come with four AA batteries. To turn your scale on, insert the batteries into the battery compartment located on the bottom side of the scale. For more help, please refer to your Participant Guide.</p>\n</div>\n<h3>Devices</h3>\n<button class=\"accordion\">What do I do if a device becomes uncomfortable?</button>\n<div class=\"panel\">\n  <p>Stop wearing the device if it becomes uncomfortable or if you have a skin reaction (like a rash), and\n  <a href=\"https://api.4youandme.net/app/api/v1/contact\" target=\"_top\">contact</a> a member of the study team.\n  </p>\n</div>\n<button class=\"accordion\">What do I do if one of the devices breaks or stops working?</button>\n<div class=\"panel\">\n  <p>\n  <a href=\"https://api.4youandme.net/app/api/v1/contact\" target=\"_top\">Contact</a> a member of the study team.\n  </p>\n</div>\n<h3>RescueTime App</h3>\n<button class=\"accordion\">Do I need to share my location data?</button>\n<div class=\"panel\">\n  <p>Yes. Location Tracking is how RescueTime tracks device use. RescueTime does send location data to their surveys and this data is not shared with 4YouandMe or any of its partners.</p>\n</div>\n<button class=\"accordion\">How do I share my location data?</button>\n<div class=\"panel\">\n  <p>You can go into Settings -> RescueTime -> Location and set \" Location \" to \" Always \".</p>\n</div>\n<h3>Pooply by HealthMode App </h3>\n<button class=\"accordion\">How do I use the Pooply app?</button>\n<div class=\"panel\">\n  <p> Open the Pooply app and enter your passcode. On the day of an event, select \" Add Event\".</p>\n</div>\n<h3>Surveys</h3>\n<button class=\"accordion\"> How will I know when I have a new survey to complete?</button>\n<div class=\"panel\">\n  <p>Surveys will be sent out daily between 10:00 AM and 6:00 PM. You can see available surveys and activities on your feed in the study app. If notifications are enabled on your iPhone, you will receive daily reminders to complete your surveys.</p>\n</div>\n<button class=\"accordion\">What do I do if I don't want to answer a certain survey question?</button>\n<div class=\"panel\">\n  <p>Skip the question. You do not have to answer any questions you don't want to answer.</p>\n</div>\n<h3>Compensation and Points</h3>\n<button class=\"accordion\"> Will I be compensated for participating in the study?</button>\n<div class=\"panel\">\n  <p>You will be paid up to a total of $520 (£420) over the course of the study. You can also keep the Oura Ring when you complete the study.</p>\n</div>\n<button class=\"accordion\">How can I learn more about earning points and compensation?</button>\n<div class=\"panel\">\n  <p>\n    <a href=\"https://api.4youandme.dev/app/api/v1/points\" target=\"_top\">Click here</a> for information about the points and compensation system.</p>\n</div>\n<button class=\"accordion\"> Will I earn points if I don\'t complete an activity?</button>\n<div class=\"panel\">\n  <p>No! You must complete the activity for points to be earned. Activities will be monitored for indiscrepancies. For example, survey responses will be monitored for suspicious responses (e.g., all answers are identical or the survey was filled out very quickly). The awarding of points falls under the research team's discretion.</p>\n</div>\n<h3>Other</h3>\n<button class=\"accordion\">What do I do if I don\'t want to participate in the study anymore?</button>\n<div class=\"panel\">\n  <p>\n    <a href=\"https://api.4youandme.net/app/api/v1/contact\" target=\"_top\">Contact</a> a member of the research team as soon as possible.\n  </p>\n    </div>\n<script>\nvar acc = document.getElementsByClassName(\"accordion\");\nvar i;\n\nfor (i = 0; i < acc.length; i++) {\n  acc[i].addEventListener(\"click\", function() {\n    this.classList.toggle(\"active\");\n    var panel = this.nextElementSibling;\n    if (panel.style.display === \"block\") {\n      panel.style.display = \"none\";\n    } else {\n      panel.style.display = \"block\";\n    }\n  });\n}\n</script>\n\n</body>\n</html>\n"
            }
            else -> {
            }
        }

        web_view.settings.also {
            it.javaScriptEnabled = true
        }

        web_view.loadDataWithBaseURL(null, source, "text/html; charset=utf-8", "utf-8", null)
    }
}