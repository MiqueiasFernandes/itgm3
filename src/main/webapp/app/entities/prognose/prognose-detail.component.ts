import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { Prognose } from './prognose.model';
import { PrognoseService } from './prognose.service';

@Component({
    selector: 'jhi-prognose-detail',
    templateUrl: './prognose-detail.component.html'
})
export class PrognoseDetailComponent implements OnInit, OnDestroy {

    prognose: Prognose;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private prognoseService: PrognoseService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['prognose']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPrognoses();
    }

    load(id) {
        this.prognoseService.find(id).subscribe((prognose) => {
            this.prognose = prognose;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPrognoses() {
        this.eventSubscriber = this.eventManager.subscribe('prognoseListModification', (response) => this.load(this.prognose.id));
    }
}
