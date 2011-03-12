(ns vertigo.parser-test
  (:use clojure.test)
  (:require [vertigo.parser :as parser]))

(def movies-string
  "<Events>
     <Event>
       <ID>298399</ID>
       <Title>127 tuntia</Title>
       <OriginalTitle>127 Hours</OriginalTitle>
       <ProductionYear>2010</ProductionYear>
       <LengthInMinutes>95</LengthInMinutes>
       <dtLocalRelease>2011-02-04T00:00:00</dtLocalRelease>
       <Rating>K 13</Rating>
       <RatingLabel>K13</RatingLabel>
       <RatingImageUrl>
         https://media.finnkino.fi/images/rating_large_K13.png
       </RatingImageUrl>
       <LocalDistributorName>FS Film Oy</LocalDistributorName>
       <GlobalDistributorName>FS Film Oy</GlobalDistributorName>
       <ProductionCompanies>-</ProductionCompanies>
       <Genres>Seikkailu, Draama</Genres>
       <Synopsis>
         Slummien miljonääristä parhaan elokuvan Oscarilla palkitun Danny Boylen uusi elokuva perustuu uskomattomaan tositarinaan.

         127 tuntia kertoo vuorikiipeilijä Aron Ralstonin (James Franco) hurjasta seikkailusta ja hänen yrityksestään selviytyä hengissä hänen kätensä jäätyä jumiin suuren kivenlohkareen alle syrjäisessä kanjonissa Utahissa. Seuraavan viiden päivän aikana Ralston joutuu puntaroimaan elämäänsä ja taistelemaan henkensä edestä luonnonvoimien armoilla, kunnes hän lopulta kerää kaiken rohkeutensa ja nokkeluutensa vapauttaakseen itsensä ainoalla mahdollisella keinolla, laskeutuu 20-metriseltä jyrkänteeltä ja vaeltaa vielä lähes 20 kilometriä, ennen kuin pääsee lopullisesti turvaan.

         Matkansa aikana Ralston muistelee ystäviään, tyttöystäväänsä (Clémence Poésy), perhettään ja kahta retkeilijää (Amber Tamblyn ja Kate Mara), jotka hän kohtasi hieman ennen onnettomuuttaan. Olivatko he viimeiset ihmiset, jotka hän elämässään tapasi? Riipaisevan jännittävä tarina vie katsojat ennennäkemättömälle seikkailulle ja näyttää, mihin kaikkeen me henkemme pitimiksi olemmekaan valmiita.
       </Synopsis>
       <EventURL>http://www.finnkino.fi/Event/298399/</EventURL>
       <Images>
         <EventSmallImagePortrait>
           http://media.finnkino.fi/1012/Event_7559/portrait_small/127_hours_99.jpg
         </EventSmallImagePortrait>
         <EventLargeImagePortrait>
           http://media.finnkino.fi/1012/Event_7559/portrait_large/127_hours_99.jpg
         </EventLargeImagePortrait>
         <EventSmallImageLandscape>
           http://media.finnkino.fi/1012/Event_7559/landscape_small/127_hours_670.jpg
         </EventSmallImageLandscape>
         <EventLargeImageLandscape>
           http://media.finnkino.fi/1012/Event_7559/landscape_large/127_hours_670.jpg
         </EventLargeImageLandscape>
       </Images>
       <Videos>
         <EventVideo>
           <Title>Katso traileri</Title>
           <Location>
             http://fin.clip-1.filmtrailer.com/5644_16233_a_5.mp4
           </Location>
           <ThumbnailLocation>
             http://fin.image-1.filmtrailer.com/44734.jpg
           </ThumbnailLocation>
           <MediaResourceSubType>EventTrailer</MediaResourceSubType>
           <MediaResourceFormat>Flash</MediaResourceFormat>
         </EventVideo>
       </Videos>
     </Event>
   </Events>")

(def events-string
  "<Schedule xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">
     <PubDate>2011-03-13T00:00:00+02:00</PubDate>
     <Shows>
       <Show>
         <ID>176890</ID>
         <dttmShowStart>2011-03-12T10:45:00</dttmShowStart>
         <EventID>298518</EventID>
         <Title>Gnomeo &amp; Julia (dub)</Title>
         <OriginalTitle>Gnomeo &amp; Juliet (dub)</OriginalTitle>
         <ProductionYear>2011</ProductionYear>
         <LengthInMinutes>85</LengthInMinutes>
         <dtLocalRelease>2011-03-04T00:00:00</dtLocalRelease>
         <Rating>S</Rating>
         <RatingLabel>S</RatingLabel>
         <RatingImageUrl>https://media.finnkino.fi/images/rating_large_S.png</RatingImageUrl>
         <Genres>Seikkailu, Animaatio, Komedia</Genres>
         <TheatreID>1034</TheatreID>
         <TheatreAuditriumID>1266</TheatreAuditriumID>
         <Theatre>Kinopalatsi Helsinki</Theatre>
         <TheatreAuditorium>sali 9</TheatreAuditorium>
         <TheatreAndAuditorium>Kinopalatsi Helsinki, sali 9</TheatreAndAuditorium>
         <PresentationMethodAndLanguage>suomi</PresentationMethodAndLanguage>
         <PresentationMethod />
         <EventSeries />
         <ShowURL>http://www.finnkino.fi/Websales/Show/176890/</ShowURL>
         <EventURL>http://www.finnkino.fi/Event/298518/</EventURL>
         <Images>
           <EventSmallImagePortrait>http://media.finnkino.fi/1012/Event_7656/portrait_small/Gnomeo_Juliet_99.jpg</EventSmallImagePortrait>
           <EventLargeImagePortrait>http://media.finnkino.fi/1012/Event_7656/portrait_large/Gnomeo_Juliet_99.jpg</EventLargeImagePortrait>
           <EventSmallImageLandscape>http://media.finnkino.fi/1012/Event_7656/landscape_small/Gnomeo_Juliet_670.jpg</EventSmallImageLandscape>
           <EventLargeImageLandscape>http://media.finnkino.fi/1012/Event_7656/landscape_large/Gnomeo_Juliet_670.jpg</EventLargeImageLandscape>
         </Images>
       </Show>
     </Shows>
   </Schedule>")

(defn- are-these-fields-present? [m & keys]
  (->> m
       ((apply juxt keys))
       (every? identity)))
 

(deftest Movie-XML-Has-These-Fields-Parsed
  (is (-> (parser/parse-movies movies-string)
          first
          (are-these-fields-present? :id
                                     :title
                                     :original-title
                                     :synopsis
                                     :large-image-url
                                     :small-image-url
                                     :trailer-url
                                     :release-date))))

(deftest Events-XML-Has-These-Fields-Parsed
  (is (-> (parser/parse-events events-string)
          first
          (are-these-fields-present? :id
                                     :title
                                     :original-title
                                     :theatre
                                     :date
                                     :genres))))


