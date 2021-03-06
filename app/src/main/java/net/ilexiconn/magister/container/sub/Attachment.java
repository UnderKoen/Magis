/*
 * Copyright 2016 Bas van den Boom 'Z3r0byte'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ilexiconn.magister.container.sub;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attachment implements Serializable {
    @SerializedName("Id")
    public int id;

    @SerializedName("Naam")
    public String fileName;

    @SerializedName("ContentType")
    public String contentType;

    @SerializedName("Datum")
    public String uploadDate;

    @SerializedName("Grootte")
    public long fileSizeInBytes;

    @SerializedName("UniqueId")
    public String uniqueId;

    @SerializedName("BronSoort")
    public int sourceType;

    @SerializedName("Links")
    public Link[] links;
}
