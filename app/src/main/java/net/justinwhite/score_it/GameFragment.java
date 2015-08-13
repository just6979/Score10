/*
 * Copyright (c) 2015 Justin White <jw@justinwhite.net>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

package net.justinwhite.score_it;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.justinwhite.score_model.phase_10.Phase10GameModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameFragment
        extends Fragment
        implements YesNoDialogFragment.YesNoDialogListener {

    @Bind(R.id.textNewNumPlayers)
    TextView textNewNumPlayers;
    @Bind(R.id.textGameName)
    TextView textGameName;
    private GameSetupListener gameSetupListener;
    private Phase10GameModel game;

    public static GameFragment newInstance() {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        game = new Phase10GameModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, rootView);

        int numPlayers = ((MainActivity) getActivity()).getNumPlayers();
        game.setNumPlayers(numPlayers);
        textGameName.setText(game.getName());
        textNewNumPlayers.setText(Integer.toString(numPlayers));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            gameSetupListener = (GameSetupListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GameSetupListener");
        }
    }

    @OnClick(R.id.buttonEndGame)
    protected void EndGame(View view) {
        FragmentManager fm = getActivity().getFragmentManager();
        YesNoDialogFragment endGameDialog = YesNoDialogFragment.newInstance(
                getString(R.string.dialog_end_game_title),
                getString(R.string.dialog_end_game_text)
        );
        endGameDialog.setTargetFragment(this, 0);
        endGameDialog.show(fm, "end_game_dialog");

    }

    @Override
    public void YesNoSubmit() {
        Fragment newFragment = CreateGameFragment.newInstance();
        gameSetupListener.setCurrentFragmentID(MainActivity.FRAG_ID_CREATE_GAME);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, newFragment)
                .commit()
        ;

    }
}
