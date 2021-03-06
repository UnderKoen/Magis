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

package com.z3r0byte.magis.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.z3r0byte.magis.Adapters.NewGradesAdapter;
import com.z3r0byte.magis.R;
import com.z3r0byte.magis.Utils.DB_Handlers.NewGradesDB;
import com.z3r0byte.magis.Utils.ErrorViewConfigs;
import com.z3r0byte.magis.Utils.MagisFragment;

import net.ilexiconn.magister.Magister;
import net.ilexiconn.magister.container.Grade;
import net.ilexiconn.magister.handler.GradeHandler;

import java.io.IOException;
import java.security.InvalidParameterException;


public class NewGradesTask extends AsyncTask<Void, Void, Grade[]> {
    private static final String TAG = "NewGradesTask";


    public MagisFragment fragment;
    public Magister magister;

    public String error = null;


    public NewGradesTask(MagisFragment fragment, Magister magister) {
        Log.d(TAG, "NewGradesTask() called with: " + "fragment = [" + fragment + "], magister = [" + magister + "]");
        this.fragment = fragment;
        this.magister = magister;

    }

    @Override
    protected void onPreExecute() {
        fragment.mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected Grade[] doInBackground(Void... params) {
        try {
            if (magister == null) {
                throw new InvalidParameterException("Niet ingelogd");
            }
            GradeHandler gradeHandler = new GradeHandler(magister);
            Grade[] grades = gradeHandler.getRecentGrades();
            NewGradesDB db = new NewGradesDB(fragment.getActivity());
            db.addGrades(grades);

            Log.d(TAG, "doInBackground: Amount of new Grades: " + grades.length);
            return grades;
        } catch (IOException e) {
            Log.e(TAG, "Unable to retrieve data", e);
            error = fragment.getString(R.string.err_no_connection);
            return null;
        } catch (InvalidParameterException e) {
            Log.e(TAG, "Invalid Parameters", e);
            error = fragment.getString(R.string.err_unknown);
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Grade[] grades) {
        if (error == null) {
            if (grades != null && grades.length > 0) {
                Log.d(TAG, "onPostExecute: Valid grades");
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragment.grades = grades;
                        fragment.mNewGradesAdapter = new NewGradesAdapter(fragment.getActivity(), fragment.grades);
                        fragment.listView.setAdapter(fragment.mNewGradesAdapter);
                        fragment.errorView.setVisibility(View.GONE);
                        fragment.mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            } else {
                Log.d(TAG, "onPostExecute: Invalid grades");
                fragment.mSwipeRefreshLayout.setRefreshing(false);
                fragment.errorView.setVisibility(View.VISIBLE);
                fragment.errorView.setConfig(ErrorViewConfigs.NoNewGradesConfig);
            }
        } else {
            Log.d(TAG, "onPostExecute: Error! " + error);
            fragment.mSwipeRefreshLayout.setRefreshing(false);
            fragment.errorView.setVisibility(View.VISIBLE);
            fragment.errorView.setConfig(ErrorViewConfigs.NoNewGradesConfig);
        }
    }
}
