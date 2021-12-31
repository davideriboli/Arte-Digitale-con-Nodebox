; Midimport - Import MIDI files as simple data structures.
; Copyright 2012 Frederik De Bleser
; Licensed under the MIT License. See the LICENSE file for details.

(ns midi
  (:import [java.io File]
           [javax.sound.midi MidiSystem]))

;http://www.onicos.com/staff/iz/formats/midi-event.html

(def note-events
  {0x80 {:chan 1  :note "off"}
   0x81 {:chan 2  :note "off"}
   0x82 {:chan 3  :note "off"}
   0x83 {:chan 4  :note "off"}
   0x84 {:chan 5  :note "off"}
   0x85 {:chan 6  :note "off"}
   0x86 {:chan 7  :note "off"}
   0x87 {:chan 8  :note "off"}
   0x88 {:chan 9  :note "off"}
   0x89 {:chan 10 :note "off"}
   0x8A {:chan 11 :note "off"}
   0x8B {:chan 12 :note "off"}
   0x8C {:chan 13 :note "off"}
   0x8D {:chan 14 :note "off"}
   0x8E {:chan 15 :note "off"}
   0x8F {:chan 16 :note "off"}
   0x90 {:chan 1  :note "on"}
   0x91 {:chan 2  :note "on"}
   0x92 {:chan 3  :note "on"}
   0x93 {:chan 4  :note "on"}
   0x94 {:chan 5  :note "on"}
   0x95 {:chan 6  :note "on"}
   0x96 {:chan 7  :note "on"}
   0x97 {:chan 8  :note "on"}
   0x98 {:chan 9  :note "on"}
   0x99 {:chan 10 :note "on"}
   0x9A {:chan 11 :note "on"}
   0x9B {:chan 12 :note "on"}
   0x9C {:chan 13 :note "on"}
   0x9D {:chan 14 :note "on"}
   0x9E {:chan 15 :note "on"}
   0x9F {:chan 16 :note "on"}})

(defn decode-event [e]
  (let [msg (.getMessage e)]
    {:tick (.getTick e)
     :status (.getStatus msg)
     :data (vec (.getMessage msg))}))

(defn events-for-track [track]
  (let [sz (.size track)
        r (range 0 sz)]
    (map #(decode-event (.get track %)) r)))

(defn decode-note [e]
  (let [d (:data e)
        m (note-events (:status e))]
    {:tick (:tick e)
     :chan (:chan m)
     :note (:note m)
     :pitch (d 1)
     :velocity (d 2)}))
  
(defn notes-for-track [track]
  (let [events (events-for-track track)
        note-events (filter #(contains? note-events (:status %)) events)]
    (map decode-note note-events)))




; for each note..
; record if we are playing it at the moment
; if we are not, add it to the map (channel + pitch + tick)
; if we are, calculate the duration and add it to a new list (channel / pitch / tick /duration)


;(def notes [{:tick 9600 :pitch 35 :vel 100} {:tick 9600 :pitch 23 :vel 100 } {:tick 9960 :pitch 23 :vel 0} {:tick 9720 :pitch 35 :vel 0}])

(defn calculate-durations [all-notes]
  (loop [notes all-notes
         notes-playing {}
         durations []]
    (if notes
      (let [note (first notes)
            note-key [(:chan note) (:pitch note)]]
        (if-let [start-note (notes-playing note-key)]
          ; The note is currently playing.
          (recur (next notes)
                 ; Remove the note from the list of notes playing
                 (dissoc notes-playing note-key)
                 ; Add the note to the output list and calculate the duration.
                 ; Use all values from the start-note.
                 (conj durations
                   {:tick (:tick start-note)
                    :chan (:chan start-note)
                    :pitch (:pitch start-note)
                    :velocity (:velocity start-note)
                    :duration (- (:tick note) (:tick start-note))}))
          ; The note is not playing.
          (recur (next notes)
                 ; Add it to the list of notes playing.
                 (assoc notes-playing note-key note)
                 durations)))
    durations)))

(defn notes-for-file [fname]
  (let [f (java.io.File. fname)
        ms (MidiSystem/getSequence f)
        tracks (seq (.getTracks ms))]
  (calculate-durations (flatten (map notes-for-track tracks)))))


(defn stringify-map-keys [m]
  "Convert a map that uses keywords as keys to a map with string keys."
  (into {} 
    (for [[k v] m] [(name k) v])))

(defn notes-for-file-java [fname]
  (map stringify-map-keys (notes-for-file fname)))



(def nodes [{:name "notes-for-file-java" :fn notes-for-file-java}])
