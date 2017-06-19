import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { ItgmTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PrognoseDetailComponent } from '../../../../../../main/webapp/app/entities/prognose/prognose-detail.component';
import { PrognoseService } from '../../../../../../main/webapp/app/entities/prognose/prognose.service';
import { Prognose } from '../../../../../../main/webapp/app/entities/prognose/prognose.model';

describe('Component Tests', () => {

    describe('Prognose Management Detail Component', () => {
        let comp: PrognoseDetailComponent;
        let fixture: ComponentFixture<PrognoseDetailComponent>;
        let service: PrognoseService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ItgmTestModule],
                declarations: [PrognoseDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PrognoseService,
                    EventManager
                ]
            }).overrideComponent(PrognoseDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PrognoseDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PrognoseService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Prognose(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.prognose).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
