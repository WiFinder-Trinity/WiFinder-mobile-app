/**
* Wifinder REST APIs
* This page lists all the rest APIs for Wifinder App.
*
* OpenAPI spec version: 0.0.1-SNAPSHOT
* 
*
* NOTE: This class is auto generated by the swagger code generator program.
* https://github.com/swagger-api/swagger-codegen.git
* Do not edit the class manually.
*/
package fr.wifinder.wifinderandroid.models

/**
 * 
 * @param hotspots 
 * @param latitude 
 * @param longitude 
 */
data class FindInput (
        val hotspots: kotlin.Array<HotspotInformation>? = null,
        val latitude: kotlin.Double? = null,
        val longitude: kotlin.Double? = null
) {

}

